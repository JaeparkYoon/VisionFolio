package jpyoon.example.visionfolio.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import jpyoon.example.visionfolio.designsystem.R

object NotificationChannels {
    const val DAILY_SUMMARY = "daily_summary"
    const val HEADLINE = "headline"
    const val PRICE_ALERT = "price_alert"

    fun ensureCreated(context: Context) {
        val manager = context.getSystemService<NotificationManager>() ?: return
        val channels = listOf(
            NotificationChannel(
                DAILY_SUMMARY,
                context.getString(R.string.notif_daily_summary_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply { description = context.getString(R.string.notif_daily_summary_desc) },
            NotificationChannel(
                HEADLINE,
                context.getString(R.string.notif_headline_name),
                NotificationManager.IMPORTANCE_LOW,
            ).apply { description = context.getString(R.string.notif_headline_desc) },
            NotificationChannel(
                PRICE_ALERT,
                context.getString(R.string.notif_price_alert_name),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply { description = context.getString(R.string.notif_price_alert_desc) },
        )
        manager.createNotificationChannels(channels)
    }
}
