package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.core.utils.changeLocalDateToGraphDate
import com.example.bodymeasurement.core.utils.roundToDecimal
import java.time.LocalDate

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    bodyPartsValue: List<BodyPartsValue>,
    pathAndCircleWidth: Float = 5f,
    pathAndCircleColor: Color = MaterialTheme.colorScheme.primary,
    helperLineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
) {

    val dataPointValues = bodyPartsValue.asReversed().map { it.value }
    val dates = bodyPartsValue.asReversed().map { it.date.changeLocalDateToGraphDate() }

    val textMeasurer = rememberTextMeasurer()

    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfParts = 3
    val difference = (highestValue - lowestValue) / noOfParts
    val valuesList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    val firstDate = dates.firstOrNull() ?: ""
    val date2 = dates.getOrNull((dates.size * 0.33).toInt()) ?: ""
    val date3 = dates.getOrNull((dates.size * 0.67).toInt()) ?: ""
    val lastDate = dates.lastOrNull() ?: ""
    val datesList = listOf(firstDate, date2, date3, lastDate)

    val animationProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = Unit) {
        animationProgress.animateTo(targetValue = 1f, tween(durationMillis = 3000))
    }

    Canvas(modifier = modifier) {
        val graphWidth = size.width
        val graphHeight = size.height
        val points = calculatePoints(dataPointValues, graphWidth, graphHeight)
        val path = createPath(points)
        val filledPath = createFilledPath(path, graphHeight, graphWidth)

        valuesList.forEachIndexed { index, value ->
            val graph80PercentHeight = graphHeight * 0.8f
            val graph5PercentHeight = graphHeight * 0.05f
            val graph10PercentWidth = graphWidth * 0.1f
            val xPosition = 0f
            val yPosition = (graph80PercentHeight / noOfParts) * index
            drawText(
                textMeasurer = textMeasurer,
                text = "$value",
                style = textStyle,
                topLeft = Offset(x = xPosition, y = yPosition)
            )
            drawLine(
                color = helperLineColor,
                strokeWidth = pathAndCircleWidth,
                start = Offset(x = graph10PercentWidth, y = (yPosition + graph5PercentHeight)),
                end = Offset(x = graphWidth, y = (yPosition + graph5PercentHeight))
            )
        }

        datesList.forEachIndexed { index, date ->
            val graph10PercentWidth = graphWidth * 0.1f
            val graph77PercentWidth = graphWidth * 0.77f
            val xPosition = (graph77PercentWidth / noOfParts) * index + graph10PercentWidth
            val yPosition = graphHeight * 0.9f
            drawText(
                textMeasurer = textMeasurer,
                text = date,
                style = textStyle,
                topLeft = Offset(x = xPosition, y = yPosition)
            )
        }

        clipRect(right = graphWidth * animationProgress.value) {
            points.forEach { point ->
                drawCircle(
                    color = pathAndCircleColor,
                    radius = pathAndCircleWidth,
                    center = point
                )
            }

            drawPath(
                path = path,
                color = pathAndCircleColor,
                style = Stroke(pathAndCircleWidth)
            )

            if (dataPointValues.isNotEmpty()) {
                drawPath(
                    path = filledPath,
                    brush = Brush.verticalGradient(
                        listOf(pathAndCircleColor.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
            }
        }
    }
}


fun createFilledPath(
    path: Path,
    graphWidth: Float,
    graphHeight: Float
): Path {
    val filledPath = Path()
    val graph85PercentHeight = graphHeight * 0.85f
    val graph10PercentWidth = graphWidth * 0.1f

    filledPath.addPath(path)
    filledPath.lineTo(graphWidth, graph85PercentHeight)
    filledPath.lineTo(graph10PercentWidth, graph85PercentHeight)
    filledPath.close()
    return filledPath
}

fun createPath(
    points: List<Offset>
): Path {
    val path = Path()
    if (points.isNotEmpty()) {
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val currentPoint = points[i]
            val previousPoint = points[i - 1]
            val controlPointX = (previousPoint.x + currentPoint.x) / 2f
            path.cubicTo(
                controlPointX, previousPoint.y,
                controlPointX, currentPoint.y,
                currentPoint.x, currentPoint.y
            )
        }
    }
    return path
}

fun calculatePoints(
    dataPoint: List<Float>,
    graphWidth: Float,
    graphHeight: Float
): List<Offset> {
    val graph90PercentWidth = graphWidth * 0.9f
    val graph10PercentWidth = graphWidth * 0.1f

    val graph80PercentHeight = graphHeight * 0.8f
    val graph5PercentHeight = graphHeight * 0.05f

    val highestValue = dataPoint.maxOrNull() ?: 0f
    val lowestValue = dataPoint.minOrNull() ?: 0f
    val valueRange = highestValue - lowestValue

    val xPositions = dataPoint.indices.map { index ->
        (graph90PercentWidth / (dataPoint.size - 1)) * index + graph10PercentWidth
    }
    val yPositions = dataPoint.map { value ->
        //Normalize each data point to a value between 0 and 1.
        val normalizedValue = (value - lowestValue) / valueRange
        val yPosition = (graph80PercentHeight * (1 - normalizedValue)) + graph5PercentHeight
        yPosition
    }
//    val yPositions = List(xPositions.size) { 0f }
    return xPositions.zip(yPositions) { x, y -> Offset(x, y) }
}


@Preview(showBackground = true)
@Composable
private fun LineGraphPreview() {
    val dummyBodyPartValues = listOf(
        BodyPartsValue(value = 72.0f, date = LocalDate.of(2023, 5, 10)),
        BodyPartsValue(value = 76.84865f, date = LocalDate.of(2023, 5, 1)),
        BodyPartsValue(value = 74.0f, date = LocalDate.of(2023, 4, 20)),
        BodyPartsValue(value = 75.1f, date = LocalDate.of(2023, 4, 5)),
        BodyPartsValue(value = 66.3f, date = LocalDate.of(2023, 3, 15)),
        BodyPartsValue(value = 67.2f, date = LocalDate.of(2023, 3, 10)),
        BodyPartsValue(value = 73.5f, date = LocalDate.of(2023, 3, 1)),
        BodyPartsValue(value = 69.8f, date = LocalDate.of(2023, 2, 18)),
        BodyPartsValue(value = 68.4f, date = LocalDate.of(2023, 2, 1)),
        BodyPartsValue(value = 72.0f, date = LocalDate.of(2023, 1, 22)),
        BodyPartsValue(value = 70.5f, date = LocalDate.of(2023, 1, 14))
    )
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 2 / 1f)
            .padding(16.dp),
        bodyPartsValue = dummyBodyPartValues
    )
}
