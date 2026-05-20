package jpyoon.example.visionfolio.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.UserProfile
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import java.text.DateFormat
import java.util.Date

@Composable
fun ProfileCard(
    profile: UserProfile,
    holdingCount: Int,
    lastSyncAt: Long,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    VfCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accent.wash, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    profile.initial.take(1),
                    color = accent.ink,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f),
            ) {
                Text(
                    profile.displayName.ifBlank { stringResource(R.string.default_user_name) },
                    style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W600),
                    color = VfColors.InkPrimary,
                )
                Text(
                    text = stringResource(R.string.profile_summary, holdingCount, formatLastSync(lastSyncAt, stringResource(R.string.label_no_sync))),
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
            }
        }
    }
}

private fun formatLastSync(ts: Long, noSyncLabel: String): String = when {
    ts <= 0L -> noSyncLabel
    else -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(ts))
}
