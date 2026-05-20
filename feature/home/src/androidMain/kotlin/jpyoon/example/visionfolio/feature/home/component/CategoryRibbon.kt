package jpyoon.example.visionfolio.feature.home.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.component.VfChipRow
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.foundation.VfCategoryColors
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.displayName
import kotlin.math.cos
import kotlin.math.sin

private data class DonutEntry(val label: String, val value: Long, val color: Color)

@Composable
fun CategoryRibbon(
    byCategory: Map<AssetCategory, Long>,
    total: Long,
    holdings: List<EnrichedHolding> = emptyList(),
    displayCurrency: Currency = Currency.KRW,
    modifier: Modifier = Modifier,
) {
    var showHoldings by remember { mutableStateOf(false) }

    val donutEntries = remember(byCategory, holdings, showHoldings) {
        if (showHoldings) {
            holdings
                .filter { it.valueKrw > 0 }
                .sortedByDescending { it.valueKrw }
                .map { DonutEntry(it.holding.name, it.valueKrw, VfCategoryColors.of(it.holding.category)) }
        } else {
            byCategory.entries
                .filter { it.value > 0 }
                .sortedByDescending { it.value }
                .map { (cat, value) -> DonutEntry(cat.displayName, value, VfCategoryColors.of(cat)) }
        }
    }

    VfCard(modifier = modifier) {
        if (holdings.isNotEmpty()) {
            VfChipRow(modifier = Modifier.padding(bottom = 8.dp)) {
                VfChip(
                    text = stringResource(R.string.chip_category),
                    selected = !showHoldings,
                    onClick = { showHoldings = false },
                )
                VfChip(
                    text = stringResource(R.string.chip_holding),
                    selected = showHoldings,
                    onClick = { showHoldings = true },
                )
            }
        }

        if (donutEntries.isEmpty() || total == 0L) {
            Text(
                stringResource(R.string.empty_category),
                style = VfTypography.BodyItem,
                color = VfColors.InkTertiary,
            )
            return@VfCard
        }

        // Donut chart with center text overlay
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            val gapDegrees = 0f
            val availableDegrees = 360f

            val textMeasurer = rememberTextMeasurer()
            val labelStyle = remember {
                TextStyle(fontSize = 11.sp, color = Color.White, textAlign = TextAlign.Center)
            }
            val segments = remember(donutEntries, total) {
                donutEntries.map { entry ->
                    val sweep = (entry.value.toFloat() / total.toFloat()) * availableDegrees
                    Triple(entry, entry.value, sweep)
                }
            }

            Canvas(modifier = Modifier.size(230.dp)) {
                val strokeWidth = 48.dp.toPx()
                val inset = strokeWidth / 2f
                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                val topLeft = Offset(inset, inset)
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val labelRadius = (size.width - strokeWidth) / 2f

                var startAngle = -90f // start from top
                segments.forEach { (entry, _, sweep) ->
                    drawArc(
                        color = entry.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    )

                    // Draw label if segment is large enough
                    if (sweep >= 25f) {
                        val midAngle = Math.toRadians((startAngle + sweep / 2f).toDouble())
                        val labelX = centerX + labelRadius * cos(midAngle).toFloat()
                        val labelY = centerY + labelRadius * sin(midAngle).toFloat()
                        val label = splitLabel(entry.label)
                        val measured = textMeasurer.measure(label, labelStyle)
                        drawText(
                            textLayoutResult = measured,
                            topLeft = Offset(
                                labelX - measured.size.width / 2f,
                                labelY - measured.size.height / 2f,
                            ),
                        )
                    }

                    startAngle += sweep + gapDegrees
                }
            }

            // Center text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.label_total_assets),
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
                Text(
                    text = CurrencyFormatter.format(total, displayCurrency, compact = true),
                    style = VfTypography.HeadingSection,
                    color = VfColors.InkPrimary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Legend (2-column grid)
        Column(
            modifier = Modifier.padding(top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            donutEntries.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { entry ->
                        DonutLegendItem(
                            entry = entry,
                            total = total,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (pair.size == 1) Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private fun splitLabel(name: String): String {
    val truncated = if (name.length > 4) name.take(4) + "…" else name
    if (truncated.length <= 3) return truncated
    val first = truncated.length / 2
    return truncated.substring(0, first) + "\n" + truncated.substring(first)
}

@Composable
private fun DonutLegendItem(
    entry: DonutEntry,
    total: Long,
    modifier: Modifier = Modifier,
) {
    val percent = entry.value.toDouble() / total.toDouble() * 100.0
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(entry.color),
        )
        Text(
            text = entry.label,
            style = VfTypography.BodyItem,
            color = VfColors.InkPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp).weight(1f),
        )
        Text(
            text = PercentFormatter.format(percent, alwaysSign = false),
            style = VfTypography.MetaSub,
            color = VfColors.InkSecondary,
        )
    }
}
