package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasuringDatePicker(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    dateState: DatePickerState,
    onDismissRequest: () -> Unit,
    confirmTextButton: String = "yes",
    dismissTextButton: String = "Cancel",
    onConfirmButtonClick: () -> Unit
) {

    if (isOpen) {
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmButtonClick() }
                ) {
                    Text(text = confirmTextButton)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(text = dismissTextButton)
                }
            },
            content = {
                DatePicker(state = dateState)
            }
        )
    }

}