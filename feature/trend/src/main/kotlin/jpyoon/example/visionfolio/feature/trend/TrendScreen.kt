package jpyoon.example.visionfolio.feature.trend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.designsystem.component.SectionHeader
import jpyoon.example.visionfolio.feature.trend.component.AiSummaryCard
import jpyoon.example.visionfolio.feature.trend.component.ChartCard
import jpyoon.example.visionfolio.feature.trend.component.ContributionCard
import jpyoon.example.visionfolio.feature.trend.component.PeriodChipRow
import jpyoon.example.visionfolio.feature.trend.component.StatsGrid
import jpyoon.example.visionfolio.feature.trend.event.TrendEvents

@Composable
fun TrendScreen(
    state: TrendState,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(stringResource(R.string.title_trend), style = VfTypography.TitleScreen, color = VfColors.InkPrimary)
            Text(
                stringResource(R.string.subtitle_trend),
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        PeriodChipRow(
            selected = state.period,
            available = state.availablePeriods,
            onSelect = { onEvent(TrendEvents.ClickedPeriod(it)) },
            onOpenCustom = { onEvent(TrendEvents.ClickedOpenCustomPicker) },
        )

        ChartCard(
            series = state.series,
            stats = state.stats,
            hoveredIndex = state.hoveredIndex,
            onHover = { onEvent(TrendEvents.HoveredAt(it)) },
            displayCurrency = state.displayCurrency,
            usdKrw = state.usdKrw,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        if (state.stats != null) {
            Spacer(Modifier.height(8.dp))

            SectionHeader(title = stringResource(R.string.section_interval_stats), subtitle = periodLabel(state.period))
            StatsGrid(
                stats = state.stats,
                displayCurrency = state.displayCurrency,
                usdKrw = state.usdKrw,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        if (state.contributions.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))

            SectionHeader(title = stringResource(R.string.section_contribution), subtitle = stringResource(R.string.section_contribution_subtitle))
            ContributionCard(
                contributions = state.contributions,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        AiSummaryCard(
            summary = state.aiSummary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun periodLabel(p: Period): String = when (p) {
    Period.D1 -> stringResource(R.string.period_d1)
    Period.W1 -> stringResource(R.string.period_w1)
    Period.M1 -> stringResource(R.string.period_m1)
    Period.M3 -> stringResource(R.string.period_m3)
    Period.M6 -> stringResource(R.string.period_m6)
    Period.Y1 -> stringResource(R.string.period_y1)
    Period.ALL -> stringResource(R.string.period_all)
    Period.CUSTOM -> stringResource(R.string.period_custom)
}
