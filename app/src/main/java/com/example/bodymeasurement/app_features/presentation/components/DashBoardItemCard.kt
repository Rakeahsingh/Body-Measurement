package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.BodyPart

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart,
    onItemClick: (String) -> Unit
) {

    Card(
        modifier = modifier,
        onClick = {
            bodyPart.bodyPartId?.let { onItemClick(it) }
        }
    ) {

        Row(
            modifier = modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(8f),
                text = bodyPart.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${bodyPart.latestValue ?: ""} ${bodyPart.measuringUnit}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.size(10.dp))

            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp)
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "show details",
                    modifier = Modifier.size(15.dp)
                )
            }

        }

    }

}