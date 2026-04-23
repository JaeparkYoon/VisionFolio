package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfCategoryColors
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import kotlin.math.absoluteValue

@Composable
fun ContributionCard(
    contributions: List<CategoryContribution>,
    modifier: Modifier = Modifier,
) {
    val maxAbs = contributions.maxOfOrNull { it.changePct.absoluteValue } ?: 1.0
    val accent = LocalAccent.current

    VfCard(modifier = modifier) {
        contributions.forEachIndexed { index, c ->
            if (index > 0) Box(modifier = Modifier.height(10.dp))
            ContributionRow(
                contribution = c,
                maxAbs = maxAbs,
                upColor = accent.base,
                downColor = VfColors.Down,
            )
        }
    }
}

@Composable
private fun ContributionRow(
    contribution: CategoryContribution,
    maxAbs: Double,
    upColor: Color,
    downColor: Color,
) {
    val up = contribution.changePct >= 0
    val pct = contribution.changePct
    val fraction = (pct.absoluteValue / maxAbs).coerceIn(0.0, 1.0).toFloat()
    val barColor = if (up) upColor else downColor

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 6.dp, height = 26.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(VfCategoryColors.of(contribution.category)),
            )
            Text(
                text = contribution.category.displayName,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
            )
            Text(
                text = PercentFormatter.format(pct),
                style = VfTypography.BodyItem,
                color = if (up) VfColors.Up else VfColors.Down,
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(VfColors.LineSoft),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(barColor),
            )
        }
    }
}
