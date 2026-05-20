package jpyoon.example.visionfolio.feature.dividend

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.dividend.event.DividendEvents

class DividendEventDispatcher(
    private val viewModel: DividendViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            DividendEvents.ViewedDividend -> eventTracker.send(EventTrackingType.VIEWED_DIVIDEND)
            is DividendEvents.ClickedOpenNews -> viewModel.dispatch(DividendIntent.OpenNews(events.id))
            is DividendEvents.ClickedOpenIndex -> viewModel.dispatch(DividendIntent.OpenIndex(events.name))
            is DividendEvents.ClickedOpenGuru -> {
                eventTracker.send(EventTrackingType.CLICKED_OPEN_GURU)
                viewModel.dispatch(DividendIntent.OpenGuru(events.guruId))
            }
            else -> Unit
        }
    }
}
