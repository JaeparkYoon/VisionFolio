package jpyoon.example.visionfolio.feature.trend

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingCode
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.trend.event.TrendEvents
import org.json.JSONObject

class TrendEventDispatcher(
    private val viewModel: TrendViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            TrendEvents.ViewedTrend -> eventTracker.send(EventTrackingType.VIEWED_TREND)
            is TrendEvents.ClickedPeriod -> {
                viewModel.dispatch(TrendIntent.SelectPeriod(events.period))
                eventTracker.send(
                    EventTrackingType.CLICKED_TREND_PERIOD,
                    JSONObject().put(EventTrackingCode.PERIOD, events.period.name),
                )
            }
            is TrendEvents.ClickedCustomRange -> {
                viewModel.dispatch(TrendIntent.SelectCustomRange(events.from, events.to))
                eventTracker.send(EventTrackingType.CLICKED_TREND_CUSTOM_RANGE)
            }
            is TrendEvents.HoveredAt -> viewModel.dispatch(TrendIntent.HoverAt(events.index))
            TrendEvents.ClickedOpenCustomPicker -> viewModel.dispatch(TrendIntent.OpenCustomPicker)
            else -> Unit
        }
    }
}
