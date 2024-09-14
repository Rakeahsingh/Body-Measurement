package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.TimeRange

@Composable
fun ChartTimeRangeButton(
    modifier: Modifier = Modifier,
    selectedTimeRange: TimeRange,
    onClick: (TimeRange) -> Unit
) {

    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        TimeRange.entries.forEach { timeRange ->

            TimeRangeSelectedButton(
                modifier = Modifier.weight(1f),
                label = timeRange.label,
                textStyle = if (timeRange == selectedTimeRange) MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        else MaterialTheme.typography.labelLarge.copy(color = Color.Gray),
                backgroundColor = if (timeRange == selectedTimeRange) MaterialTheme.colorScheme.surface
                         else Color.Transparent,
                onClick = { onClick(timeRange) }
            )

        }

    }
    
}


@Composable
fun TimeRangeSelectedButton(
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle,
    backgroundColor: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){

        Text(
            text = label,
            style = textStyle,
            maxLines = 1
        )

    }

}