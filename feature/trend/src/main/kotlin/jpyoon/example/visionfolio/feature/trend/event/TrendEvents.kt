package jpyoon.example.visionfolio.feature.trend.event

import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.domain.model.Period

sealed interface TrendEvents : Events {
    data object ViewedTrend : TrendEvents
    data class ClickedPeriod(val period: Period) : TrendEvents
    data class ClickedCustomRange(val from: Long, val to: Long) : TrendEvents
    data class HoveredAt(val index: Int?) : TrendEvents
    data object ClickedOpenCustomPicker : TrendEvents
}
