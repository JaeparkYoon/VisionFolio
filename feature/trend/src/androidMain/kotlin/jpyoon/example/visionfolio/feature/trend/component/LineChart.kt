package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.domain.model.SeriesPoint
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LineChart(
    points: List<SeriesPoint>,
    accent: Color,
    hoveredIndex: Int?,
    onHover: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    referenceLines: Int = 4,
    yRangePadding: Float = 0f,
    yLabelFormatter: (Long) -> String,
) {
    val textMeasurer = rememberTextMeasurer()
    val refColor = VfColors.LineSoft
    val inkTertiary = VfColors.InkTertiary
    val inkPrimary = VfColors.InkPrimary
    val bgDefault = VfColors.BgDefault
    val labelFontSize = VfTypography.MetaSubSm.fontSize
    val tooltipFontSize = VfTypography.MetaSub.fontSize
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("yyyy.M.d", Locale.KOREAN).withZone(ZoneId.systemDefault())
    }

    val density = LocalDensity.current
    val chartLeft = remember(points, referenceLines, yRangePadding, yLabelFormatter, textMeasurer) {
        if (points.size < 2) 0f
        else {
            val values = points.map { it.valueKrw }
            val rawMin = values.min()
            val rawMax = values.max()
            val rawRange = (rawMax - rawMin).coerceAtLeast(1L)
            val pad = (rawRange * yRangePadding).toLong()
            val mn = (rawMin - pad).coerceAtLeast(0L)
            val mx = rawMax + pad
            var maxWidth = 0
            for (i in 0..referenceLines) {
                val labelValue = mx - ((mx - mn) * i / referenceLines)
                val layout = textMeasurer.measure(
                    text = yLabelFormatter(labelValue),
                    style = TextStyle(fontSize = labelFontSize),
                )
                maxWidth = maxOf(maxWidth, layout.size.width)
            }
            maxWidth.toFloat() + with(density) { 8.dp.toPx() }
        }
    }

    Canvas(
        modifier = modifier
            .pointerInput(points.size, chartLeft) {
                detectDragGestures(
                    onDragStart = { pos -> onHover(indexFor(pos.x - chartLeft, size.width.toFloat() - chartLeft, points.size)) },
                    onDragEnd = { onHover(null) },
                    onDragCancel = { onHover(null) },
                ) { change, _ ->
                    onHover(indexFor(change.position.x - chartLeft, size.width.toFloat() - chartLeft, points.size))
                }
            }
            .pointerInput(points.size, chartLeft) {
                detectTapGestures(
                    onTap = { pos -> onHover(indexFor(pos.x - chartLeft, size.width.toFloat() - chartLeft, points.size)) },
                )
            },
    ) {
        if (points.size < 2) return@Canvas
        val padYPx = 24.dp.toPx()
        val labelMargin = 4.dp.toPx()
        val refStroke = 1.dp.toPx()
        val values = points.map { it.valueKrw }
        val rawMin = values.min()
        val rawMax = values.max()
        val rawRange = (rawMax - rawMin).coerceAtLeast(1L)
        val yPad = (rawRange * yRangePadding).toLong()
        val min = (rawMin - yPad).coerceAtLeast(0L)
        val max = rawMax + yPad
        val range = max - min
        val chartWidth = size.width - chartLeft
        val stepX = chartWidth / (points.size - 1)
        val innerH = size.height - padYPx * 2

        for (i in 0..referenceLines) {
            val y = padYPx + innerH * (i.toFloat() / referenceLines)
            drawLine(
                color = refColor,
                start = Offset(chartLeft, y),
                end = Offset(size.width, y),
                strokeWidth = refStroke,
            )
            val labelValue = max - ((max - min) * i / referenceLines)
            val layout = textMeasurer.measure(
                text = yLabelFormatter(labelValue),
                style = TextStyle(fontSize = labelFontSize, color = inkTertiary),
            )
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(0f, y - layout.size.height - labelMargin),
            )
        }

        val coords = points.mapIndexed { i, p ->
            val x = chartLeft + i * stepX
            val y = padYPx + innerH - ((p.valueKrw - min).toFloat() / range.toFloat()) * innerH
            Offset(x, y)
        }
        val line = Path().apply { moveTo(coords.first().x, coords.first().y) }
        val area = Path().apply {
            moveTo(coords.first().x, size.height - padYPx)
            lineTo(coords.first().x, coords.first().y)
        }
        val smoothing = 6f
        for (i in 0 until coords.size - 1) {
            val p0 = coords[(i - 1).coerceAtLeast(0)]
            val p1 = coords[i]
            val p2 = coords[i + 1]
            val p3 = coords[(i + 2).coerceAtMost(coords.lastIndex)]
            val cp1x = p1.x + (p2.x - p0.x) / smoothing
            val cp1y = p1.y + (p2.y - p0.y) / smoothing
            val cp2x = p2.x - (p3.x - p1.x) / smoothing
            val cp2y = p2.y - (p3.y - p1.y) / smoothing
            line.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
            area.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
        }
        area.lineTo(coords.last().x, size.height - padYPx)
        area.close()

        drawPath(
            area,
            brush = Brush.verticalGradient(
                colors = listOf(accent.copy(alpha = 0.22f), accent.copy(alpha = 0f)),
                startY = padYPx,
                endY = size.height - padYPx,
            ),
        )
        drawPath(
            line,
            color = accent,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )

        hoveredIndex?.takeIf { it in points.indices }?.let { idx ->
            val x = chartLeft + idx * stepX
            val p = points[idx]
            val y = padYPx + innerH - ((p.valueKrw - min).toFloat() / range.toFloat()) * innerH
            val dotRadius = 5.dp.toPx()
            val dashInterval = 3.dp.toPx()

            drawLine(
                color = inkTertiary,
                start = Offset(x, padYPx),
                end = Offset(x, size.height - padYPx),
                strokeWidth = refStroke,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashInterval, dashInterval * 1.5f)),
            )
            drawCircle(color = bgDefault, radius = dotRadius, center = Offset(x, y))
            drawCircle(
                color = accent,
                radius = dotRadius,
                center = Offset(x, y),
                style = Stroke(width = 2.dp.toPx()),
            )

            val dateText = dateFormatter.format(Instant.ofEpochMilli(p.timestamp))
            val valueText = yLabelFormatter(p.valueKrw)
            val tipLayout = textMeasurer.measure(
                text = "$dateText · $valueText",
                style = TextStyle(
                    fontSize = tooltipFontSize,
                    color = inkPrimary,
                    fontWeight = FontWeight.W600,
                ),
            )
            val padX = 10.dp.toPx()
            val padTipY = 6.dp.toPx()
            val tipGap = 10.dp.toPx()
            val boxW = tipLayout.size.width + padX * 2
            val boxH = tipLayout.size.height + padTipY * 2
            val boxX = (x - boxW / 2f).coerceIn(0f, size.width - boxW)
            val boxY = (y - boxH - tipGap).coerceAtLeast(labelMargin)

            drawRoundRect(
                color = bgDefault,
                topLeft = Offset(boxX, boxY),
                size = Size(boxW, boxH),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
            )
            drawRoundRect(
                color = refColor,
                topLeft = Offset(boxX, boxY),
                size = Size(boxW, boxH),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(width = refStroke),
            )
            drawText(
                textLayoutResult = tipLayout,
                topLeft = Offset(boxX + padX, boxY + padTipY),
            )
        }
    }
}

private fun indexFor(x: Float, width: Float, count: Int): Int {
    if (count < 2) return 0
    val step = width / (count - 1)
    return (x / step).toInt().coerceIn(0, count - 1)
}
