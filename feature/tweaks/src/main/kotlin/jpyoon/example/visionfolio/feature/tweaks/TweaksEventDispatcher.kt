package jpyoon.example.visionfolio.feature.tweaks

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.tweaks.event.TweaksEvents

class TweaksEventDispatcher(
    private val viewModel: TweaksViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            TweaksEvents.ViewedTweaks -> eventTracker.send(EventTrackingType.VIEWED_TWEAKS)
            else -> Unit
        }
    }
}
