package jpyoon.example.visionfolio.feature.home

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingCode
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.home.event.HomeEvents
import org.json.JSONObject

/**
 * 홈 화면 이벤트 디스패처. UI 이벤트를 ViewModel Intent + 분석 추적으로 라우팅합니다.
 */
class HomeEventDispatcher(
    private val viewModel: HomeViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            HomeEvents.ViewedHome -> {
                eventTracker.send(EventTrackingType.VIEWED_HOME)
            }
            HomeEvents.ClickedToggleHideAmounts -> {
                viewModel.dispatch(HomeIntent.ToggleHideAmounts)
                eventTracker.send(EventTrackingType.CLICKED_TOGGLE_HIDE_AMOUNTS)
            }
            HomeEvents.ClickedToggleDisplayCurrency -> {
                viewModel.dispatch(HomeIntent.ToggleDisplayCurrency)
                eventTracker.send(EventTrackingType.CLICKED_TOGGLE_DISPLAY_CURRENCY)
            }
            is HomeEvents.ClickedFilterCategory -> {
                viewModel.dispatch(HomeIntent.FilterCategory(events.category))
                eventTracker.send(
                    EventTrackingType.CLICKED_FILTER_CATEGORY,
                    JSONObject().put(EventTrackingCode.CATEGORY, events.category?.name ?: "ALL"),
                )
            }
            HomeEvents.ClickedUpload -> {
                viewModel.dispatch(HomeIntent.OpenUploadSheet)
                eventTracker.send(EventTrackingType.CLICKED_UPLOAD)
            }
            HomeEvents.ClickedOpenHoldings -> {
                viewModel.dispatch(HomeIntent.OpenAllHoldings)
                eventTracker.send(EventTrackingType.CLICKED_OPEN_HOLDINGS)
            }
            is HomeEvents.ClickedTab -> {
                viewModel.dispatch(HomeIntent.SelectTab(events.tab))
                eventTracker.send(
                    EventTrackingType.CLICKED_DIVIDEND_TAB,
                    JSONObject().put(EventTrackingCode.TAB, events.tab.name),
                )
            }
            HomeEvents.ClickedOpenTrend -> {
                viewModel.dispatch(HomeIntent.OpenTrend)
                eventTracker.send(EventTrackingType.CLICKED_OPEN_TREND)
            }
            else -> Unit
        }
    }
}
