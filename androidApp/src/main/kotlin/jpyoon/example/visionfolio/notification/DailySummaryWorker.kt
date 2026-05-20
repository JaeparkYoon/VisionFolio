package jpyoon.example.visionfolio.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import jpyoon.example.visionfolio.R
import jpyoon.example.visionfolio.app.MainActivity
import jpyoon.example.visionfolio.app.VisionFolioApp
import jpyoon.example.visionfolio.domain.model.formatter.KrwFormatter
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import kotlinx.coroutines.flow.first

class DailySummaryWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = runCatching {
        val component = (applicationContext as VisionFolioApp).appComponent
        val observeHomeData = component.observeHomeDataUseCase
        val data = observeHomeData().first()
        val summary = data.summary
        val title = "오늘의 자산 요약"
        val body = "평가액 ${KrwFormatter.format(summary.totalValue, compact = true)} · " +
            "오늘 ${PercentFormatter.format(summary.todayPct)}"

        notify(title = title, body = body)
        Result.success()
    }.getOrElse { Result.retry() }

    private fun notify(title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val pending = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notif = NotificationCompat.Builder(applicationContext, NotificationChannels.DAILY_SUMMARY)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notif)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        const val UNIQUE_NAME = "daily_summary_worker"
    }
}
