package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnonymousSignInButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    isEnabled: Boolean = true,
    primaryText: String = "Continue Without Loading..",
    secondaryText: String = "Please Wait...",
    onClick: () -> Unit
) {

    var buttonText by remember {
        mutableStateOf(primaryText)
    }

    LaunchedEffect(key1 = loadingState) {
       buttonText =  if (loadingState) secondaryText else primaryText
    }

    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
    ) {

        Text(text = buttonText)
        
        if (loadingState){
            Spacer(modifier = modifier.width(8.dp))
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }

}