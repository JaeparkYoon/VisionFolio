package jpyoon.example.visionfolio.core.analytics.event

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Compose 트리에서 사용할 이벤트 트래커.
 */
val LocalEventTracker = staticCompositionLocalOf<EventTracker> { NoOpEventTracker() }
