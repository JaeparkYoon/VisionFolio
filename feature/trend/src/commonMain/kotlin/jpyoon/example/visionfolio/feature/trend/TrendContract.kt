package jpyoon.example.visionfolio.feature.trend

import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.ErrorState
import jpyoon.example.visionfolio.domain.model.IntervalStats
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries

data class TrendState(
    val isLoading: Boolean = false,
    val period: Period = Period.M1,
    val customRange: ClosedRange<Long>? = null,
    val series: PortfolioSeries? = null,
    val stats: IntervalStats? = null,
    val hoveredIndex: Int? = null,
    val contributions: List<CategoryContribution> = emptyList(),
    val aiSummary: String? = null,
    val error: ErrorState? = null,
    val displayCurrency: Currency = Currency.KRW,
    val usdKrw: Double = 1388.0,
    /** 스냅샷 축적 기간에 따라 노출해야 하는 period 목록. */
    val availablePeriods: List<Period> = listOf(Period.D1, Period.ALL, Period.CUSTOM),
) : ViewState

sealed interface TrendIntent : ViewIntent {
    data class SelectPeriod(val period: Period) : TrendIntent
    data class SelectCustomRange(val from: Long, val to: Long) : TrendIntent
    data class HoverAt(val index: Int?) : TrendIntent
    object DismissCustomPicker : TrendIntent
    object OpenCustomPicker : TrendIntent
}

sealed interface TrendEffect : ViewEffect {
    object ShowCustomRangeSheet : TrendEffect
    object DismissCustomRangeSheet : TrendEffect
}
