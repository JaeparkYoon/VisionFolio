package jpyoon.example.visionfolio.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

sealed interface RowTrailing {
    data class Text(val value: String) : RowTrailing
    data class Toggle(val checked: Boolean, val onChange: (Boolean) -> Unit) : RowTrailing
    data class Custom(val content: @Composable () -> Unit) : RowTrailing
    object Chevron : RowTrailing
    object None : RowTrailing
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    headline: String,
    trailing: RowTrailing = RowTrailing.Chevron,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(VfShapes.Sm)
                    .background(VfColors.BgAlt),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VfColors.InkSecondary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = headline,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        when (trailing) {
            is RowTrailing.Text -> Text(
                text = trailing.value,
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
            is RowTrailing.Toggle -> jpyoon.example.visionfolio.designsystem.component.VfToggle(
                checked = trailing.checked,
                onCheckedChange = trailing.onChange,
            )
            is RowTrailing.Custom -> trailing.content()
            RowTrailing.Chevron -> Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = VfColors.InkTertiary,
                modifier = Modifier.size(20.dp),
            )
            RowTrailing.None -> Unit
        }
    }
}
