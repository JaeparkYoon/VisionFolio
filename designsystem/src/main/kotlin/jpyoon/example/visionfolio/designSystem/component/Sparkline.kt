package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Sparkline(
    points: List<Double>,
    color: Color,
    modifier: Modifier = Modifier.size(width = 24.dp, height = 14.dp),
    strokeWidthDp: Float = 1.5f,
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas
        val min = points.min()
        val max = points.max()
        val range = (max - min).takeIf { it > 0.0 } ?: 1.0
        val stepX = size.width / (points.size - 1)
        val path = Path()
        points.forEachIndexed { i, value ->
            val x = i * stepX
            val y = size.height - ((value - min) / range * size.height).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidthDp.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}
