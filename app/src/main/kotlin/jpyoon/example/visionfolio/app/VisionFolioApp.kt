package jpyoon.example.visionfolio.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import jpyoon.example.visionfolio.data.di.AppCoroutineScope
import jpyoon.example.visionfolio.data.portfolio.SnapshotRecorder
import jpyoon.example.visionfolio.notification.NotificationChannels
import jpyoon.example.visionfolio.notification.NotificationScheduler
import jpyoon.example.visionfolio.data.repository.AppPrefsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class VisionFolioApp : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var appPrefsRepo: AppPrefsRepository
    @Inject lateinit var notificationScheduler: NotificationScheduler
    @Inject lateinit var snapshotRecorder: SnapshotRecorder

    @Inject
    @AppCoroutineScope
    lateinit var appScope: CoroutineScope

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header(
                                "User-Agent",
                                "VisionFolio/1.0 (Android; https://github.com/JaeparkYoon/VisionFolio)",
                            )
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(true)
            .build()

    override fun onCreate() {
        super.onCreate()
        NotificationChannels.ensureCreated(this)

        snapshotRecorder.start(appScope)

        appPrefsRepo.observeNotifications()
            .map { it.dailySummary }
            .distinctUntilChanged()
            .onEach { enabled ->
                if (enabled) notificationScheduler.scheduleDailySummary()
                else notificationScheduler.cancelDailySummary()
            }
            .launchIn(appScope)
    }
}
