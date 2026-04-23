package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun GuruProfileImage(
    imageUrl: String,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        loading = { ProfileFallback(name = name, size = size) },
        error = { ProfileFallback(name = name, size = size) },
    )
}

@Composable
private fun ProfileFallback(name: String, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(VfColors.BgAlt),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            name.take(1),
            style = VfTypography.HeadingSection,
            color = VfColors.InkTertiary,
        )
    }
}
