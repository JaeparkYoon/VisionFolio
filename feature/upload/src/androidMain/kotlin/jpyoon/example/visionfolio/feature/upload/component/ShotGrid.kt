package jpyoon.example.visionfolio.feature.upload.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import androidx.compose.ui.res.stringResource
import jpyoon.example.visionfolio.designsystem.R

@Composable
fun ShotGrid(
    shots: List<ScreenshotRef>,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles: List<TileItem> = shots.mapIndexed { i, ref -> TileItem.Shot(i, ref) } + TileItem.Add

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tiles.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { tile ->
                    when (tile) {
                        is TileItem.Shot -> ShotTile(
                            index = tile.index,
                            ref = tile.ref,
                            onRemove = { onRemove(tile.index) },
                            modifier = Modifier.weight(1f),
                        )
                        TileItem.Add -> AddTile(onClick = onAdd, modifier = Modifier.weight(1f))
                    }
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun ShotTile(
    index: Int,
    ref: ScreenshotRef,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(10.dp))
            .background(VfColors.BgAlt),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(ref.uri).crossfade(true).build(),
            contentDescription = stringResource(R.string.cd_screenshot_number, index + 1),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .padding(6.dp)
                .size(22.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = stringResource(R.string.cd_remove),
                tint = Color.White,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun AddTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, VfColors.LineDefault), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = stringResource(R.string.cd_add),
            tint = VfColors.InkSecondary,
            modifier = Modifier.size(24.dp),
        )
    }
}

private sealed interface TileItem {
    data class Shot(val index: Int, val ref: ScreenshotRef) : TileItem
    object Add : TileItem
}
