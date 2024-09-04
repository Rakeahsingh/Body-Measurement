package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddItemCard(
    modifier: Modifier = Modifier,
    name: String,
    isChecked: Boolean,
    onCheckedChanged: () -> Unit,
    onCardClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clickable { onCardClick() },
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(8f)
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = isChecked,
                onCheckedChange = { onCheckedChanged() }
            )
        }

    }

}