package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun AiSummaryCard(
    summary: String?,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    VfCard(
        modifier = modifier,
        color = accent.wash,
        border = BorderStroke(1.dp, accent.dim.copy(alpha = 0.33f)),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(accent.base, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = VfColors.BgDefault,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                text = summary ?: stringResource(R.string.ai_summary_loading),
                style = VfTypography.BodyDefault,
                color = accent.ink,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
            )
        }
    }
}
