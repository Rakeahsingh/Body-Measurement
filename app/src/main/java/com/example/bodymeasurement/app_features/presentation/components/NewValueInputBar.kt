package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.bodymeasurement.R
import kotlin.math.sin

@Composable
fun NewValueInputBar(
    modifier: Modifier = Modifier,
    value: String,
    date: String,
    onValueChange: (String) -> Unit,
    onDoneImeActionClick: () -> Unit,
    onCalenderIconClick: () -> Unit
) {

    var inputError by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    inputError = when{
        value.isBlank() -> "Please Enter new value here"
        value.toFloatOrNull() == null -> "Invalid number."
        value.toFloat() < 1f -> "Please set at least 1."
        value.toFloat() > 1000f -> "Please set a maximum of 1000."
        else -> null
    }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = { onDoneImeActionClick() }),
        isError = inputError != null && value.isNotBlank(),
        supportingText = { Text(text = inputError.orEmpty())},
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = date)

                IconButton(
                    onClick = { onCalenderIconClick() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.img_2),
                        contentDescription = "calender icon"
                    )
                }
            }
        }
    )

}