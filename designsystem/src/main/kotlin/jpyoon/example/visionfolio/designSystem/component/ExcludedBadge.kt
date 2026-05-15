package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors

@Composable
fun ExcludedBadge(modifier: Modifier = Modifier) {
    Text(
        text = "배분 제외",
        fontSize = 10.sp,
        color = VfColors.InkTertiary,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(VfColors.BgAlt)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}
