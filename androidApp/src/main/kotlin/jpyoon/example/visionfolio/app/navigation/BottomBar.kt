package jpyoon.example.visionfolio.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.designsystem.foundation.AccentColors
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.navigation.TopDestination

@Composable
fun BottomBar(
    currentRoute: String?,
    onSelect: (TopDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(24.dp), clip = false)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, VfColors.LineDefault, RoundedCornerShape(24.dp))
                .background(color = VfColors.BgDefault, shape = RoundedCornerShape(24.dp))
                .padding(5.dp),
        ) {
            TopDestination.entries.forEach { dest ->
                val selected = dest.route == currentRoute
                NavCell(
                    dest = dest,
                    selected = selected,
                    accent = accent,
                    onClick = { onSelect(dest) },
                )
            }
        }
    }
}

@Composable
private fun NavCell(
    dest: TopDestination,
    selected: Boolean,
    accent: AccentColors,
    onClick: () -> Unit,
) {
    val bg = if (selected) accent.wash else Color.Transparent
    val tint = if (selected) accent.ink else VfColors.InkSecondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .defaultMinSize(minWidth = 74.dp)
            .padding(horizontal = 18.dp, vertical = 8.dp),
    ) {
        Icon(
            imageVector = dest.icon,
            contentDescription = dest.label,
            tint = tint,
            modifier = Modifier.size(19.dp),
        )
        Text(
            text = dest.label,
            color = tint,
            style = VfTypography.MetaSubSm.copy(
                fontSize = 10.5.sp,
                lineHeight = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            ),
        )
    }
}
