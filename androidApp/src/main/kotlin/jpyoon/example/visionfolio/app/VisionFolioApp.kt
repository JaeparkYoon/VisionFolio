package jpyoon.example.visionfolio.app

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import jpyoon.example.visionfolio.app.di.AppComponent
import jpyoon.example.visionfolio.app.di.create
import jpyoon.example.visionfolio.core.repository.api.ScreenshotParser
import jpyoon.example.visionfolio.data.ai.ScreenshotParseResultStore
import jpyoon.example.visionfolio.data.ai.ScreenshotParseSession
import jpyoon.example.visionfolio.data.ai.ScreenshotParseServiceDeps
import jpyoon.example.visionfolio.notification.NotificationChannels
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import timber.log.Timber

class VisionFolioApp : Application(), Configuration.Provider, ImageLoaderFactory, ScreenshotParseServiceDeps {

    val appComponent: AppComponent by lazy { AppComponent::class.create(this) }

    override val screenshotParser: ScreenshotParser get() = appComponent.screenshotParser
    override val screenshotParseSession: ScreenshotParseSession get() = appComponent.screenshotParseSession
    override val screenshotParseResultStore: ScreenshotParseResultStore get() = appComponent.screenshotParseResultStore

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
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
        if (jpyoon.example.visionfolio.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        NotificationChannels.ensureCreated(this)

        val scope = appComponent.appScope
        appComponent.snapshotRecorder.start(scope)

        val notificationScheduler = appComponent.notificationScheduler
        appComponent.appPrefsRepository.observeNotifications()
            .map { it.dailySummary }
            .distinctUntilChanged()
            .onEach { enabled ->
                if (enabled) notificationScheduler.scheduleDailySummary()
                else notificationScheduler.cancelDailySummary()
            }
            .launchIn(scope)
    }
}
