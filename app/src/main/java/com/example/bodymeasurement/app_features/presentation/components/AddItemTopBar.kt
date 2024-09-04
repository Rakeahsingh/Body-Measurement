package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemTopBar(
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit,
    onAddIconClick: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = "Add Item")
        },
        windowInsets = WindowInsets(0,0,0,0),
        navigationIcon = {
            IconButton(
                onClick = { onBackIconClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onAddIconClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add icon"
                )
            }
        }
    )

}
