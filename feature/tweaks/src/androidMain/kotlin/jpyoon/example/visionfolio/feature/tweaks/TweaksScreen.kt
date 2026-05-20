package jpyoon.example.visionfolio.feature.tweaks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.designsystem.foundation.AccentColors
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfAccentPalette
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

private val Presets = listOf(
    AccentPreset.SALMON to R.string.preset_salmon,
    AccentPreset.TERRACOTTA to R.string.preset_terracotta,
    AccentPreset.CORAL to R.string.preset_coral,
    AccentPreset.BURGUNDY to R.string.preset_burgundy,
)

@Composable
fun TweaksScreen(
    state: TweaksState,
    onIntent: (TweaksIntent) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VfCard(
        modifier = modifier.width(260.dp),
        shape = VfShapes.Lg,
        padding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.title_tweaks), style = VfTypography.HeadingSection, color = VfColors.InkPrimary)
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.cd_close),
                    tint = VfColors.InkSecondary,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Text(
            stringResource(R.string.label_accent_tone),
            style = VfTypography.LabelCaps,
            color = VfColors.InkTertiary,
            modifier = Modifier.padding(top = 14.dp),
        )

        PresetGrid(
            selected = state.accent,
            onSelect = { onIntent(TweaksIntent.SelectAccent(it)) },
            modifier = Modifier.padding(top = 10.dp),
        )

        HelperCard(modifier = Modifier.padding(top = 14.dp))
    }
}

@Composable
private fun PresetGrid(
    selected: AccentPreset,
    onSelect: (AccentPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Presets.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (preset, labelRes) ->
                    PresetButton(
                        preset = preset,
                        label = stringResource(labelRes),
                        active = preset == selected,
                        onClick = { onSelect(preset) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetButton(
    preset: AccentPreset,
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette: AccentColors = VfAccentPalette.of(preset)
    val activeAccent = LocalAccent.current
    val borderColor = if (active) activeAccent.base else VfColors.LineDefault
    Row(
        modifier = modifier
            .clip(VfShapes.Pill)
            .background(if (active) activeAccent.wash else Color.Transparent, VfShapes.Pill)
            .border(BorderStroke(1.dp, borderColor), VfShapes.Pill)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(palette.base, CircleShape),
        )
        Text(
            label,
            style = VfTypography.BodyItem,
            color = if (active) activeAccent.ink else VfColors.InkPrimary,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun HelperCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(VfShapes.Md)
            .background(VfColors.BgAlt)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            stringResource(R.string.helper_accent),
            style = VfTypography.MetaSub,
            color = VfColors.InkSecondary,
        )
    }
}
