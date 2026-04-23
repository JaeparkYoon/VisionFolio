package jpyoon.example.visionfolio.feature.upload.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

@Composable
fun SparkleSpinner(
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    val transition = rememberInfiniteTransition(label = "sparkle")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1_000, easing = LinearEasing)),
        label = "rotation",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.size(68.dp).rotate(angle),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(68.dp)) {
                drawArc(
                    color = accent.base,
                    startAngle = -90f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                )
            }
            Icon(
                Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = accent.ink,
                modifier = Modifier.size(28.dp).rotate(-angle),
            )
        }

        RollingQuotes(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )
    }
}

@Composable
private fun RollingQuotes(modifier: Modifier = Modifier) {
    val allQuotes = stringArrayResource(R.array.wall_street_quotes)
    val shuffled = remember { allQuotes.toList().shuffled() }
    val itemHeight = 40.dp
    val density = LocalDensity.current
    val itemHeightPx = remember(density) { with(density) { itemHeight.toPx() } }

    val scrollStep = remember { Animatable(0f) }
    val cubicBezier = remember { CubicBezierEasing(0.65f, 0f, 0.35f, 1f) }

    // Build a window of 3 items: [current, next, current+2(=fake wrap)]
    val currentIndex = remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val messages = remember(currentIndex.intValue) {
        val i = currentIndex.intValue % shuffled.size
        val j = (i + 1) % shuffled.size
        listOf(shuffled[i], shuffled[j], shuffled[(j + 1) % shuffled.size])
    }

    LaunchedEffect(Unit) {
        delay(10_000)
        while (isActive) {
            // Scroll from item 0 -> item 1
            scrollStep.animateTo(1f, animationSpec = tween(500, easing = cubicBezier))
            delay(10_000)
            // Advance index, snap back to 0
            currentIndex.intValue += 1
            scrollStep.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .height(itemHeight)
            .clipToBounds(),
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(align = Alignment.Top, unbounded = true)
                .offset { IntOffset(x = 0, y = -(scrollStep.value * itemHeightPx).roundToInt()) },
        ) {
            messages.forEach { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = msg,
                        style = VfTypography.MetaSub,
                        color = VfColors.InkSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
