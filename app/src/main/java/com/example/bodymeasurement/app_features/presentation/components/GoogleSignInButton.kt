package com.example.bodymeasurement.app_features.presentation.components


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.R

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    isEnabled: Boolean = true,
    primaryText: String = "Sign In With Google",
    secondaryText: String = "Please Wait...",
    onClick: () -> Unit
) {

    var textButton by remember {
        mutableStateOf(primaryText)
    }

    LaunchedEffect(key1 = loadingState) {
        textButton = if (loadingState) secondaryText else primaryText
    }

    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "google icon",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = textButton)

        if (loadingState) {
            Spacer(modifier = Modifier.width(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }



    }

}

