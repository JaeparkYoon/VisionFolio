package jpyoon.example.visionfolio.feature.home.event

import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.designsystem.component.DividendTab
import jpyoon.example.visionfolio.domain.model.AssetCategory

/**
 * 홈 화면에서 발생하는 이벤트.
 */
sealed interface HomeEvents : Events {
    data object ViewedHome : HomeEvents
    data object ClickedToggleHideAmounts : HomeEvents
    data object ClickedToggleDisplayCurrency : HomeEvents
    data class ClickedFilterCategory(val category: AssetCategory?) : HomeEvents
    data object ClickedUpload : HomeEvents
    data object ClickedOpenHoldings : HomeEvents
    data class ClickedTab(val tab: DividendTab) : HomeEvents
    data object ClickedOpenTrend : HomeEvents
}
