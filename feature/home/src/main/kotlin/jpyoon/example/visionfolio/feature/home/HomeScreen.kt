package jpyoon.example.visionfolio.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.DividendSummaryCard
import jpyoon.example.visionfolio.designsystem.component.DividendTabRow
import jpyoon.example.visionfolio.designsystem.component.HoldingDividendList
import jpyoon.example.visionfolio.designsystem.component.SectionHeader
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.feature.home.component.CategoryRibbon
import jpyoon.example.visionfolio.feature.home.component.HoldingsCard
import jpyoon.example.visionfolio.feature.home.component.QuoteHeader
import jpyoon.example.visionfolio.feature.home.component.TotalCard
import jpyoon.example.visionfolio.feature.home.event.HomeEvents

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier,
    todayLabel: String = "",
) {
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        QuoteHeader(
            dateLabel = todayLabel,
        )

        TotalCard(
            summary = state.summary,
            hideAmounts = state.hideAmounts,
            displayCurrency = state.displayCurrency,
            onToggleHide = { onEvent(HomeEvents.ClickedToggleHideAmounts) },
            onToggleCurrency = { onEvent(HomeEvents.ClickedToggleDisplayCurrency) },
            onOpenUpload = { onEvent(HomeEvents.ClickedUpload) },
            onOpenTrend = { onEvent(HomeEvents.ClickedOpenTrend) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        if (!state.summary?.byCategory.isNullOrEmpty()) {
            SectionHeader(title = stringResource(R.string.section_allocation))
            CategoryRibbon(
                byCategory = state.summary.byCategory,
                total = state.summary.totalValue,
                holdings = state.holdings,
                displayCurrency = state.displayCurrency,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        if (state.holdings.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))

            HoldingsCard(
                holdings = state.holdings,
                visibleCategory = state.visibleCategory,
                hideAmounts = state.hideAmounts,
                displayCurrency = state.displayCurrency,
                onFilter = { onEvent(HomeEvents.ClickedFilterCategory(it)) },
                onOpenAll = { onEvent(HomeEvents.ClickedOpenHoldings) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        Spacer(Modifier.height(16.dp))

        SectionHeader(title = stringResource(R.string.section_dividend_summary))
        DividendSummaryCard(
            summary = state.dividendSummary,
            displayCurrency = state.displayCurrency,
            usdKrw = state.usdKrw,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Spacer(Modifier.height(16.dp))

        DividendTabRow(
            selected = state.selectedTab,
            onSelect = { onEvent(HomeEvents.ClickedTab(it)) },
        )

        SectionHeader(
            title = stringResource(R.string.section_dividend_holdings),
            subtitle = stringResource(
                R.string.dividend_holdings_count,
                state.dividendSummary?.holdings?.size ?: 0,
            ),
        )

        HoldingDividendList(
            holdings = state.dividendSummary?.holdings ?: emptyList(),
            selectedTab = state.selectedTab,
            displayCurrency = state.displayCurrency,
            usdKrw = state.usdKrw,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Spacer(Modifier.height(80.dp))
    }
}
