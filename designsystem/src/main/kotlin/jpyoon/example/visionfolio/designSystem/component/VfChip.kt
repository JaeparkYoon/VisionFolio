package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun VfChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    val bg = if (selected) accent.wash else VfColors.BgDefault
    val fg = if (selected) accent.ink else VfColors.InkSecondary
    val outline =
        if (selected) Modifier else Modifier.border(BorderStroke(1.dp, VfColors.LineDefault), VfShapes.Pill)

    Surface(
        shape = VfShapes.Pill,
        color = bg,
        contentColor = Color.Unspecified,
        modifier = modifier
            .then(outline)
            .clip(VfShapes.Pill)
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            style = VfTypography.BodyItem,
            color = fg,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        )
    }
}

@Composable
fun VfChipRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) { content() }
}
