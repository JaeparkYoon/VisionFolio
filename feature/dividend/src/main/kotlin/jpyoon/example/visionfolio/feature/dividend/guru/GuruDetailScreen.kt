package jpyoon.example.visionfolio.feature.dividend.guru

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.AssetBadge
import jpyoon.example.visionfolio.designsystem.component.GuruProfileImage
import jpyoon.example.visionfolio.designsystem.component.SectionHeader
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.GuruHolding
import jpyoon.example.visionfolio.domain.model.GuruProfile
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuruDetailScreen(
    guru: GuruProfile,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VfColors.BgDefault),
    ) {
        TopAppBar(
            title = {
                Text(
                    guru.name,
                    style = VfTypography.HeadingSection,
                    color = VfColors.InkPrimary,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.btn_close),
                        tint = VfColors.InkPrimary,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = VfColors.BgDefault,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            /* ── 프로필 카드 ── */
            GuruProfileCard(
                guru = guru,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp))

            /* ── 자산배분 도넛 ── */
            SectionHeader(title = stringResource(R.string.section_allocation))
            GuruAllocationChart(
                holdings = guru.holdings,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Spacer(Modifier.height(16.dp))

            /* ── 보유자산 ── */
            SectionHeader(
                title = stringResource(R.string.label_holdings),
                subtitle = stringResource(R.string.guru_holdings_count, guru.holdings.size),
            )
            GuruHoldingsCard(
                holdings = guru.holdings,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

/* ── 프로필 카드 ── */

@Composable
private fun GuruProfileCard(
    guru: GuruProfile,
    modifier: Modifier = Modifier,
) {
    VfCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GuruProfileImage(
                imageUrl = guru.imageUrl,
                name = guru.name,
                size = 80.dp,
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    guru.name,
                    style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W600),
                    color = VfColors.InkPrimary,
                )
                Text(
                    guru.title,
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        Text(
            guru.philosophy,
            style = VfTypography.BodyItem,
            color = VfColors.InkSecondary,
            modifier = Modifier.padding(top = 14.dp),
        )
    }
}

/* ── 자산배분 도넛차트 ── */

private data class DonutEntry(
    val label: String,
    val shortLabel: String,
    val value: Long,
    val color: Color,
)

private val StockPalette: List<Color> = listOf(
    Color(0xFFD98574),
    Color(0xFFC49A6C),
    Color(0xFF8FA58C),
    Color(0xFFB57A8A),
    Color(0xFF7B8FA1),
    Color(0xFFA89B85),
    Color(0xFFC06051),
    Color(0xFFD9927F),
    Color(0xFFE88A7A),
    Color(0xFF8A7F72),
)

@Composable
private fun GuruAllocationChart(
    holdings: List<GuruHolding>,
    modifier: Modifier = Modifier,
) {
    val totalValue = remember(holdings) { holdings.sumOf { it.valueUsd } }
    val donutEntries = remember(holdings) {
        holdings
            .filter { it.valueUsd > 0 }
            .sortedByDescending { it.valueUsd }
            .mapIndexed { index, holding ->
                DonutEntry(
                    label = holding.name,
                    shortLabel = holding.code.ifBlank { holding.name },
                    value = holding.valueUsd,
                    color = StockPalette[index % StockPalette.size],
                )
            }
    }

    VfCard(modifier = modifier) {
        if (donutEntries.isEmpty() || totalValue == 0L) {
            Text(
                stringResource(R.string.empty_category),
                style = VfTypography.BodyItem,
                color = VfColors.InkTertiary,
            )
            return@VfCard
        }

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
            val segments = remember(donutEntries, totalValue) {
                donutEntries.map { entry ->
                    val sweep = (entry.value.toFloat() / totalValue.toFloat()) * availableDegrees
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

                var startAngle = -90f
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

                    if (sweep >= 18f) {
                        val midAngle = Math.toRadians((startAngle + sweep / 2f).toDouble())
                        val labelX = centerX + labelRadius * cos(midAngle).toFloat()
                        val labelY = centerY + labelRadius * sin(midAngle).toFloat()
                        val label = shortenTicker(entry.shortLabel)
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.guru_total_portfolio),
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
                Text(
                    text = CurrencyFormatter.format(totalValue, Currency.USD, compact = true),
                    style = VfTypography.HeadingSection,
                    color = VfColors.InkPrimary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Column(
            modifier = Modifier.padding(top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            donutEntries.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { entry ->
                        DonutLegendItem(
                            entry = entry,
                            total = totalValue,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (pair.size == 1) Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
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

private fun shortenTicker(code: String): String =
    if (code.length > 5) code.take(5) else code

/* ── 보유자산 카드 ── */

@Composable
private fun GuruHoldingsCard(
    holdings: List<GuruHolding>,
    modifier: Modifier = Modifier,
) {
    var visibleCategory: AssetCategory? by remember { mutableStateOf(null) }
    val categoriesInUse = remember(holdings) {
        holdings.map { it.category }.distinct()
    }
    val filtered = remember(holdings, visibleCategory) {
        if (visibleCategory == null) holdings
        else holdings.filter { it.category == visibleCategory }
    }

    VfCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(R.string.label_holdings),
                style = VfTypography.HeadingSection,
                color = VfColors.InkPrimary,
            )
            Text(
                text = stringResource(R.string.guru_holdings_count, holdings.size),
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
        }

        if (categoriesInUse.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                VfChip(
                    stringResource(R.string.chip_all),
                    selected = visibleCategory == null,
                    onClick = { visibleCategory = null },
                )
                categoriesInUse.forEach { cat ->
                    VfChip(
                        text = cat.displayName,
                        selected = visibleCategory == cat,
                        onClick = { visibleCategory = cat },
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(top = 14.dp)) {
            filtered.forEachIndexed { index, holding ->
                if (index > 0) HorizontalDivider(color = VfColors.LineSoft)
                GuruHoldingRow(holding = holding)
            }
        }
    }
}

@Composable
private fun GuruHoldingRow(holding: GuruHolding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AssetBadge(
            category = holding.category,
            initial = holding.name.take(1),
            imageUrl = if (holding.code.isNotBlank()) "https://img.logokit.com/ticker/${holding.code}" else null,
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
        ) {
            Text(
                holding.name,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
                maxLines = 1,
            )
            Text(
                text = "${holding.category.displayName} · ${holding.code}",
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                maxLines = 1,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = CurrencyFormatter.format(holding.valueUsd, Currency.USD, compact = true),
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
            )
            Text(
                text = stringResource(R.string.guru_weight_format, "%.1f".format(holding.weight * 100)),
                style = VfTypography.MetaSub,
                color = VfColors.InkSecondary,
            )
        }
    }
}
