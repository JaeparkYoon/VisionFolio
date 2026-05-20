package jpyoon.example.visionfolio.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            title,
            style = VfTypography.LabelCaps,
            color = VfColors.InkTertiary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
        )
        VfCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            padding = PaddingValues(vertical = 4.dp),
        ) { content() }
    }
}

@Composable
fun GroupDivider() {
    HorizontalDivider(
        color = VfColors.LineSoft,
        modifier = Modifier.padding(start = 64.dp),
    )
}
