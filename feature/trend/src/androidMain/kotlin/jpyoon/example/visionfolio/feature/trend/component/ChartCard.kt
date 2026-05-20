package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.IntervalStats
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ChartCard(
    series: PortfolioSeries?,
    stats: IntervalStats?,
    hoveredIndex: Int?,
    onHover: (Int?) -> Unit,
    displayCurrency: Currency = Currency.KRW,
    usdKrw: Double = 1388.0,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    val toDisplay = { krw: Long -> toDisplayValue(krw, displayCurrency, usdKrw) }
    val symbol = when (displayCurrency) { Currency.KRW -> "₩"; Currency.USD -> "$" }

    VfCard(modifier = modifier) {
        val focused = hoveredIndex?.let { series?.points?.getOrNull(it) }
        val rawValue = focused?.valueKrw ?: stats?.end ?: 0L
        val displayValue = toDisplay(rawValue)

        Text(
            text = "$symbol%,d".format(displayValue),
            style = VfTypography.DisplayTrend,
            color = VfColors.InkPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (stats != null) {
            val changeDisplay = toDisplay(stats.changeKrw)
            val up = changeDisplay >= 0
            val color = if (up) VfColors.Up else VfColors.Down
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = (if (up) "▲ " else "▼ ") +
                        CurrencyFormatter.format(changeDisplay, displayCurrency, compact = true) +
                        "  (" + PercentFormatter.format(stats.changePct) + ")",
                    style = VfTypography.BodyItem,
                    color = color,
                )
            }
        }

        val points = series?.points.orEmpty()
        if (points.size < 2) {
            ChartEmptyState(
                hasSnapshot = points.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(top = 16.dp),
            )
        } else {
            val yRangePadding = when (series?.period) {
                Period.D1 -> 0.03f
                Period.W1 -> 0.06f
                Period.M1 -> 0.12f
                Period.M3 -> 0.20f
                Period.M6 -> 0.30f
                Period.Y1 -> 0.40f
                Period.ALL -> 0.50f
                Period.CUSTOM -> 0.15f
                null -> 0.10f
            }

            LineChart(
                points = points,
                accent = accent.base,
                hoveredIndex = hoveredIndex,
                onHover = onHover,
                yRangePadding = yRangePadding,
                yLabelFormatter = { v ->
                    val dv = toDisplay(v)
                    when (displayCurrency) {
                        Currency.KRW -> when {
                            dv >= 1_000_000_000_000L -> "${dv / 1_000_000_000_000}조"
                            dv >= 100_000_000L       -> "${dv / 100_000_000}억"
                            dv >= 10_000L            -> "${dv / 10_000}만"
                            else -> "$dv"
                        }
                        Currency.USD -> when {
                            dv >= 1_000_000_000L -> "${dv / 1_000_000_000}B"
                            dv >= 1_000_000L     -> "${dv / 1_000_000}M"
                            dv >= 1_000L         -> "${dv / 1_000}K"
                            else -> "$dv"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(top = 16.dp),
            )

            XAxisLabels(
                series = series,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }
    }
}

private fun toDisplayValue(krwValue: Long, displayCurrency: Currency, usdKrw: Double): Long =
    when (displayCurrency) {
        Currency.KRW -> krwValue
        Currency.USD -> (krwValue / usdKrw).toLong()
    }

@Composable
private fun ChartEmptyState(hasSnapshot: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(
                if (hasSnapshot) R.string.chart_empty_message
                else R.string.chart_no_snapshot_message,
            ),
            style = VfTypography.BodyItem,
            color = VfColors.InkSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun XAxisLabels(series: PortfolioSeries?, modifier: Modifier = Modifier) {
    val pts = series?.points.orEmpty()
    if (pts.size < 2) return
    val zone = ZoneId.systemDefault()
    val spanDays = (pts.last().timestamp - pts.first().timestamp) / (24 * 60 * 60 * 1000)
    val pattern = if (spanDays >= 365) "yyyy.M" else "yyyy.M.d"
    val fmt = DateTimeFormatter.ofPattern(pattern, Locale.KOREAN).withZone(zone)
    val start = fmt.format(Instant.ofEpochMilli(pts.first().timestamp))
    val mid = fmt.format(Instant.ofEpochMilli(pts[pts.size / 2].timestamp))
    val end = fmt.format(Instant.ofEpochMilli(pts.last().timestamp))
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(start, style = VfTypography.MetaSubSm, color = VfColors.InkTertiary)
        Text(mid, style = VfTypography.MetaSubSm, color = VfColors.InkTertiary)
        Text(end, style = VfTypography.MetaSubSm, color = VfColors.InkTertiary)
    }
}
