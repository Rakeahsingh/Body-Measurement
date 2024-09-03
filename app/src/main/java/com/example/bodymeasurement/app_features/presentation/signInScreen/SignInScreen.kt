package com.example.bodymeasurement.app_features.presentation.signInScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.R
import com.example.bodymeasurement.app_features.presentation.components.AnonymousSignInButton
import com.example.bodymeasurement.app_features.presentation.components.GoogleSignInButton
import com.example.bodymeasurement.app_features.presentation.components.MeasurementDialog

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    windowSize: WindowWidthSizeClass,
    state: SignInState,
    onEvent: (SignInEvent) -> Unit
) {

    val context = LocalContext.current

    var isAnonymousLoginDialogOpen by remember {
        mutableStateOf(false)
    }

    MeasurementDialog(
        isOpen = isAnonymousLoginDialogOpen,
        title = "Alert Login Anonymous?",
        onConfirmButtonClick = {
            onEvent(SignInEvent.SignInAnonymously)
            isAnonymousLoginDialogOpen = false
        },
        onDismissButtonClick = { isAnonymousLoginDialogOpen = false },
        body = {
            Text(
                text = "By logging in anonymously, you will not be able to synchronize the data " +
                        "across devices or after uninstalling the app. \nAre you sure you want to proceed?"
            )
        },
        icon = {
           Icon(
               imageVector = Icons.Default.Warning,
               contentDescription = "warning icon",
               tint = Color.Red,
               modifier = Modifier.size(36.dp)
           )
        }
    )


    when(windowSize){

        WindowWidthSizeClass.Compact -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "img logo",
                    modifier = modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text = "Body MeasureMate",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "Measure progress, not perfection",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.4f))

                GoogleSignInButton(
                    loadingState = state.isGoogleSignInButtonLoading,
                    isEnabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSighInButtonLoading,
                    onClick = {
                        onEvent(SignInEvent.SignInWithGoogle(context))
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                AnonymousSignInButton(
                    loadingState = state.isAnonymousSighInButtonLoading,
                    isEnabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSighInButtonLoading,
                    onClick = {
                        isAnonymousLoginDialogOpen = true
                    }
                )

            }
        }

        else -> {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "img logo",
                        modifier = modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "Body MeasureMate",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "Measure progress, not perfection",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    GoogleSignInButton(
                        loadingState = state.isGoogleSignInButtonLoading,
                        isEnabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSighInButtonLoading,
                        onClick = {
                            onEvent(SignInEvent.SignInWithGoogle(context))
                        }
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    AnonymousSignInButton(
                        loadingState = state.isAnonymousSighInButtonLoading,
                        isEnabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSighInButtonLoading,
                        onClick = {
                            isAnonymousLoginDialogOpen = true
                        }
                    )

                }

            }

        }

    }

}