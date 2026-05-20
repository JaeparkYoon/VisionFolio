package jpyoon.example.visionfolio.feature.returns

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.Events

class ReturnsEventDispatcher(
    private val viewModel: ReturnsViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            else -> Unit
        }
    }
}
