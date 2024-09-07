package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.MeasuringUnit

@Composable
fun MeasuringUnitMenu(
    modifier: Modifier = Modifier,
    isExpended: Boolean,
    onDismissClick: () -> Unit,
    onItemClick: (MeasuringUnit) -> Unit
) {

    DropdownMenu(
        modifier = modifier,
        expanded = isExpended,
        onDismissRequest = { onDismissClick() }
    ) {

        Box(modifier = Modifier.size(width = 150.dp, height = 300.dp)){

            LazyColumn(
               contentPadding = PaddingValues(4.dp)
            ) {
                items(MeasuringUnit.entries){ measuringUnit ->
                    DropdownMenuItem(
                        text = {
                            Text(text = "${measuringUnit.label}   (${measuringUnit.code})")
                        },
                        onClick = { onItemClick(measuringUnit) }
                    )
                }
            }

        }



    }

}


