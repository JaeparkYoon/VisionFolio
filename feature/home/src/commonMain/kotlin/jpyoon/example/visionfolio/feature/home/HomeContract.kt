package jpyoon.example.visionfolio.feature.home

import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.DividendSummary
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.ErrorState
import jpyoon.example.visionfolio.domain.model.PortfolioSummary
import jpyoon.example.visionfolio.domain.model.Quote

/**
 * Dividend display period tab for the home screen.
 * Mirrors the designsystem DividendTab values so they can be used in commonMain.
 */
enum class HomeDividendTab { YEARLY, QUARTERLY, MONTHLY }

data class HomeState(
    val isLoading: Boolean = false,
    val quote: Quote? = null,
    val summary: PortfolioSummary? = null,
    val hideAmounts: Boolean = false,
    val displayCurrency: Currency = Currency.KRW,
    val holdings: List<EnrichedHolding> = emptyList(),
    val visibleCategory: AssetCategory? = null,
    val dividendSummary: DividendSummary? = null,
    val selectedTab: HomeDividendTab = HomeDividendTab.YEARLY,
    val usdKrw: Double = 1388.0,
    val error: ErrorState? = null,
) : ViewState

sealed interface HomeIntent : ViewIntent {
    object ToggleHideAmounts : HomeIntent
    object ToggleDisplayCurrency : HomeIntent
    data class FilterCategory(val category: AssetCategory?) : HomeIntent
    object OpenUploadSheet : HomeIntent
    object OpenAllHoldings : HomeIntent
    data class SelectTab(val tab: HomeDividendTab) : HomeIntent
    object OpenTrend : HomeIntent
}

sealed interface HomeEffect : ViewEffect {
    object NavigateToUpload : HomeEffect
    object NavigateToHoldings : HomeEffect
    object NavigateToTrend : HomeEffect
    data class ShowToast(val message: String) : HomeEffect
}
