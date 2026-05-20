package jpyoon.example.visionfolio.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun QuoteHeader(
    dateLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = "제작 : 윤재박",
            style = VfTypography.TitleScreen,
            color = VfColors.InkPrimary,
        )
        Text(
            text = dateLabel,
            style = VfTypography.MetaSub,
            color = VfColors.InkTertiary,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
