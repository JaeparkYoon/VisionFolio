package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import java.text.DecimalFormat

/* ── 시장 지수 ───────────────────────────────────────── */

@Composable
fun IndexCardStrip(
    indices: List<MarketIndex>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        indices.forEach { idx -> IndexCard(idx = idx, onClick = { onSelect(idx.name) }) }
    }
}

@Composable
private fun IndexCard(idx: MarketIndex, onClick: () -> Unit) {
    val accent = LocalAccent.current
    val up = idx.changePct >= 0
    val color = if (up) VfColors.Up else VfColors.Down
    Surface(
        shape = VfShapes.Md,
        color = VfColors.Card,
        border = BorderStroke(1.dp, VfColors.LineDefault),
        modifier = Modifier
            .width(150.dp)
            .clip(VfShapes.Md)
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(idx.name, style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
                Sparkline(
                    points = idx.spark,
                    color = accent.base,
                    modifier = Modifier.size(width = 24.dp, height = 14.dp),
                )
            }
            Text(
                text = DecimalFormat("#,##0.##").format(idx.value),
                style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W600),
                color = VfColors.InkPrimary,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = PercentFormatter.format(idx.changePct),
                style = VfTypography.MetaSub,
                color = color,
            )
        }
    }
}

/* ── 뉴스 ───────────────────────────────────────── */

@Composable
fun NewsSection(
    items: List<NewsItem>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    VfCard(modifier = modifier, padding = PaddingValues(horizontal = 0.dp, vertical = 4.dp)) {
        items.forEachIndexed { idx, item ->
            if (idx > 0) HorizontalDivider(color = VfColors.LineSoft)
            NewsRow(index = idx + 1, item = item, onClick = { onClick(item.id) })
        }
    }
}

@Composable
private fun NewsRow(index: Int, item: NewsItem, onClick: () -> Unit) {
    val accent = LocalAccent.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "%02d".format(index),
            style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W600),
            color = VfColors.InkTertiary,
            modifier = Modifier.width(36.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(accent.wash, VfShapes.Pill)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(
                        item.tag,
                        style = VfTypography.LabelCaps.copy(fontSize = 10.sp),
                        color = accent.ink,
                    )
                }
                Text(
                    text = "  ${item.source}",
                    style = VfTypography.MetaSubSm,
                    color = VfColors.InkTertiary,
                )
            }
            Text(
                text = item.title,
                style = VfTypography.BodyItem,
                color = VfColors.InkPrimary,
                maxLines = 2,
                modifier = Modifier.padding(top = 6.dp),
            )
            item.aiSummary?.let { summary ->
                Text(
                    text = summary,
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
