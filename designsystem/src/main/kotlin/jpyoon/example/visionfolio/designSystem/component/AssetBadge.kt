package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.designsystem.foundation.VfCategoryColors

@Composable
fun AssetBadge(
    category: AssetCategory,
    initial: String,
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    imageUrl: String? = null,
) {
    val color = VfCategoryColors.of(category)
    val shape = RoundedCornerShape(12.dp)

    if (imageUrl != null) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = initial,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(shape),
            loading = { InitialBadge(color, initial, size) },
            error = { InitialBadge(color, initial, size) },
        )
    } else {
        InitialBadge(color, initial, size, modifier)
    }
}

@Composable
private fun InitialBadge(
    color: androidx.compose.ui.graphics.Color,
    initial: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.26f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial.take(1),
            color = color,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
            ),
        )
    }
}
