package jpyoon.example.visionfolio.feature.upload.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import jpyoon.example.visionfolio.data.ai.ScreenshotParseResultStore
import jpyoon.example.visionfolio.data.ai.ScreenshotParseSession
import jpyoon.example.visionfolio.data.repository.ScreenshotParser
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScreenshotParseService : Service() {

    @Inject lateinit var parser: ScreenshotParser
    @Inject lateinit var session: ScreenshotParseSession
    @Inject lateinit var resultStore: ScreenshotParseResultStore

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var parseJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        ensureChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_CANCEL) {
            Log.d(TAG, "cancel 요청 수신")
            parseJob?.cancel()
            scope.launch { resultStore.clear() }
            session.markCancelled()
            stopForegroundCompat()
            stopSelf()
            return START_NOT_STICKY
        }

        val uris = intent?.getStringArrayListExtra(EXTRA_URIS)
        if (uris.isNullOrEmpty()) {
            Log.w(TAG, "shot uris 가 비어 있어 종료")
            stopSelf()
            return START_NOT_STICKY
        }
        val sizes = intent.getLongArrayExtra(EXTRA_SIZES) ?: LongArray(0)
        val shots = uris.mapIndexed { i, uri ->
            ScreenshotRef(uri = uri, sizeBytes = sizes.getOrNull(i) ?: 0L)
        }

        startForegroundCompat(buildNotification())

        parseJob?.cancel()
        parseJob = scope.launch {
            session.markRunning()
            runCatching {
                parser.parse(
                    shots = shots,
                    onDownloadProgress = { progress ->
                        session.markRunning(modelDownloadProgress = progress)
                    },
                    onQuotaExceeded = { attempt, max ->
                        session.markRunning(
                            modelDownloadProgress = 1f,
                            quotaRetry = ScreenshotParseSession.QuotaRetry(attempt, max),
                        )
                    },
                )
            }
                .onSuccess { result ->
                    Log.d(TAG, "파싱 성공: ${result.parsed.size}건 — 디스크 저장 후 세션 갱신")
                    // 프로세스가 곧바로 정리돼도 살아남도록 디스크에 먼저 영속화한다.
                    resultStore.save(result)
                    session.markSucceeded(result)
                }
                .onFailure { t ->
                    Log.e(TAG, "파싱 실패", t)
                    resultStore.clear()
                    session.markFailed(t)
                }

            stopForegroundCompat()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        parseJob?.cancel()
        scope.cancel()
        super.onDestroy()
    }

    private fun buildNotification(): Notification {
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val contentPendingIntent = packageManager.getLaunchIntentForPackage(packageName)
            ?.let { PendingIntent.getActivity(this, 0, it, pendingFlags) }

        val cancelPendingIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, ScreenshotParseService::class.java).apply { action = ACTION_CANCEL },
            pendingFlags,
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notif_screenshot_parse_message))
            .setSmallIcon(R.drawable.ic_notification_parse)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(0, 0, true) // indeterminate 가로 프로그레스 애니메이션
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .apply { contentPendingIntent?.let { setContentIntent(it) } }
            .addAction(0, getString(R.string.btn_cancel), cancelPendingIntent)
            .build()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService<NotificationManager>() ?: return
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notif_screenshot_parse_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = getString(R.string.notif_screenshot_parse_desc)
            setShowBadge(false)
        }
        manager.createNotificationChannel(channel)
    }

    private fun startForegroundCompat(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun stopForegroundCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
    }

    companion object {
        private const val TAG = "ParseService"
        private const val CHANNEL_ID = "screenshot_parse"
        private const val NOTIFICATION_ID = 42101

        const val ACTION_CANCEL = "jpyoon.example.visionfolio.action.PARSE_CANCEL"
        const val EXTRA_URIS = "extra.uris"
        const val EXTRA_SIZES = "extra.sizes"

        fun start(context: Context, shots: List<ScreenshotRef>) {
            val intent = Intent(context, ScreenshotParseService::class.java).apply {
                putStringArrayListExtra(EXTRA_URIS, ArrayList(shots.map { it.uri }))
                putExtra(EXTRA_SIZES, shots.map { it.sizeBytes }.toLongArray())
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun cancel(context: Context) {
            val intent = Intent(context, ScreenshotParseService::class.java).apply {
                action = ACTION_CANCEL
            }
            context.startService(intent)
        }
    }
}
