package jpyoon.example.visionfolio.feature.addholding

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.addholding.event.AddHoldingEvents

class AddHoldingEventDispatcher(
    private val viewModel: AddHoldingViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            AddHoldingEvents.ViewedAddHolding -> eventTracker.send(EventTrackingType.VIEWED_ADD_HOLDING)
            else -> Unit
        }
    }
}
