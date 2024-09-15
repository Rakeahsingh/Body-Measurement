package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.core.utils.changeLocalDateToGraphDate
import com.example.bodymeasurement.core.utils.roundToDecimal

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    bodyPartsValue: List<BodyPartsValue>,
    pathAndCircleWidth: Float = 5f,
    pathAndCircleColor: Color = MaterialTheme.colorScheme.secondary,
    helperLineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {

    val dataPointsValue = bodyPartsValue.asReversed().map { it.value }
    val dates = bodyPartsValue.asReversed().map { it.date.changeLocalDateToGraphDate() }

    val textMeasurer = rememberTextMeasurer()

    val highestValue = dataPointsValue.maxOrNull() ?: 0f
    val lowestValue = dataPointsValue.minOrNull() ?: 0f
    val noOfParts = 3
    val difference = (highestValue - lowestValue) / noOfParts

    val valueList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    val firstDate = dates.firstOrNull() ?: ""
    val date2 = dates.getOrNull((dates.size * 0.33).toInt()) ?: ""
    val date3 = dates.getOrNull((dates.size * 0.67).toInt()) ?: ""
    val lastDate = dates.lastOrNull() ?: ""

    val dateList = listOf(firstDate, date2, date3, lastDate)

    val animateProgress = remember {
        Animatable(initialValue = 0f)
    }
    
    LaunchedEffect(key1 = Unit) {
        animateProgress.animateTo(targetValue = 1f, tween(durationMillis = 3000))
    }

}