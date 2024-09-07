package com.example.bodymeasurement.app_features.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.presentation.components.DetailsTopBar
import com.example.bodymeasurement.app_features.presentation.components.MeasurementDialog
import com.example.bodymeasurement.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    state: DetailsState,
    viewModel: DetailsViewModel,
    onEvent: (DetailsEvent) -> Unit,
    onBackIconClick: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {
                    onBackIconClick()
                }
                is UiEvent.SnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed){
                        onEvent(DetailsEvent.RestoreBodyPartValue)
                    }
                }
            }
        }
    }


    var isDeleteBodyPartDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    MeasurementDialog(
        isOpen = isDeleteBodyPartDialogOpen,
        title = "Delete Body Part?",
        body = {
            Text(
                text = "Are you sure, you want to delete this Body Part? All related " +
                        "data will be permanently removed..... \nThis action can not be undone."
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete icon",
                tint = Color.Red,
                modifier = Modifier.size(36.dp)
            )
        },
        onConfirmButtonClick = {
            onEvent(DetailsEvent.DeleteBodyPart)
            isDeleteBodyPartDialogOpen = false
        },
        onDismissButtonClick = { isDeleteBodyPartDialogOpen = false }
    )




    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        DetailsTopBar(
            bodyPart = state.bodyPart,
            onBackIconClick = { onBackIconClick() },
            onAddIconClick = { /*TODO*/ },
            onDeleteIconClick = { isDeleteBodyPartDialogOpen = true },
            onEvent = onEvent
        )
        
    }

}