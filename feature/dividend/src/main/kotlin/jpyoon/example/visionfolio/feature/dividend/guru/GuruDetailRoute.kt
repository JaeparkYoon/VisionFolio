package jpyoon.example.visionfolio.feature.dividend.guru

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.domain.model.GuruData

@Composable
fun GuruDetailRoute(
    guruId: String,
    onBack: () -> Unit,
) {
    val guru = GuruData.profiles.find { it.id == guruId } ?: run {
        onBack()
        return
    }

    val eventTracker = LocalEventTracker.current
    LaunchedEffect(Unit) {
        eventTracker.send(EventTrackingType.VIEWED_GURU_DETAIL)
    }

    GuruDetailScreen(guru = guru, onBack = onBack)
}
