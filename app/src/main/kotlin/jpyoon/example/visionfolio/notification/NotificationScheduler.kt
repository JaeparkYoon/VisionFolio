package jpyoon.example.visionfolio.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    private val workManager get() = WorkManager.getInstance(context)

    fun scheduleDailySummary() {
        val initialDelay = computeDelayToNext9AM()
        val request = PeriodicWorkRequestBuilder<DailySummaryWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            DailySummaryWorker.UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    fun cancelDailySummary() {
        workManager.cancelUniqueWork(DailySummaryWorker.UNIQUE_NAME)
    }

    private fun computeDelayToNext9AM(): Duration {
        val zone = ZoneId.systemDefault()
        val now = LocalDateTime.now(zone)
        val target = now.toLocalDate().atTime(9, 0)
            .let { if (it.isAfter(now)) it else it.plusDays(1) }
        return Duration.between(now, target)
    }
}
