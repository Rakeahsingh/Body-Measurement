package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.ui.theme.BodyMeasurementTheme

@Composable
fun MeasurementDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String,
    icon: @Composable (() -> Unit)? = null,
    confirmTextButton: String = "Yes",
    dismissTextButton: String = "Cancel",
    body: @Composable (() -> Unit)? = null,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {

    if (isOpen){

        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismissButtonClick() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmButtonClick() }
                ) {
                  Text(text = confirmTextButton)
                }
            },
            icon = icon,
            title = { Text(text = title) },
            text = body,
            dismissButton = {
                TextButton(
                    onClick = { onDismissButtonClick() }
                ) {
                    Text(text = dismissTextButton)
                }
            }
        )

    }

}

