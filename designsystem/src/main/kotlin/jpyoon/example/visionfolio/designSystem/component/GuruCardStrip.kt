package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.GuruProfile

@Composable
fun GuruCardStrip(
    gurus: List<GuruProfile>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        gurus.forEach { guru ->
            GuruCard(guru = guru, onClick = { onSelect(guru.id) })
        }
    }
}

@Composable
private fun GuruCard(guru: GuruProfile, onClick: () -> Unit) {
    Surface(
        shape = VfShapes.Md,
        color = VfColors.Card,
        border = BorderStroke(1.dp, VfColors.LineDefault),
        modifier = Modifier
            .width(130.dp)
            .clip(VfShapes.Md)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubcomposeAsyncImage(
                model = guru.imageUrl,
                contentDescription = guru.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                loading = {
                    GuruInitialBadge(initial = guru.name.take(1))
                },
                error = {
                    GuruInitialBadge(initial = guru.name.take(1))
                },
            )
            Text(
                text = guru.name,
                style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W600),
                color = VfColors.InkPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                text = guru.title,
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun GuruInitialBadge(initial: String) {
    Surface(
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        color = VfColors.BgAlt,
    ) {
        androidx.compose.foundation.layout.Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initial,
                style = VfTypography.HeadingSection,
                color = VfColors.InkTertiary,
            )
        }
    }
}
