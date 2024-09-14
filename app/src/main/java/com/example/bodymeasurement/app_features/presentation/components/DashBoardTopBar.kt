package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardTopBar(
    modifier: Modifier = Modifier,
    profilePicUrl: String?,
    onProfilePicClick: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0,0,0,0),
        title = {
            Text(
                text = "Body Measurement"
            )
        },
        actions = {
            IconButton(
                onClick = { onProfilePicClick() }
            ) {
                ProfilePicHolder(
                    padding = 2.dp,
                    borderWidth = 1.5.dp,
                    placeHolderSize = 30.dp,
                    profilePicUrl = profilePicUrl
                )
            }
        }
    )


}