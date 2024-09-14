package com.example.bodymeasurement.app_features.presentation.addItem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.bodymeasurement.R
import com.example.bodymeasurement.app_features.presentation.components.AddItemCard
import com.example.bodymeasurement.app_features.presentation.components.AddItemTopBar
import com.example.bodymeasurement.app_features.presentation.components.MeasurementDialog
import com.example.bodymeasurement.core.utils.UiEvent
import com.example.bodymeasurement.ui.theme.BodyMeasurementTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddItemScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    state: AddItemState,
    onEvent: (AddItemEvent) -> Unit,
    viewModel: AddItemViewModel,
    onBackIconClick: () -> Unit
) {


    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
                is UiEvent.SnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    var isAddItemDialogOpen by remember {
        mutableStateOf(false)
    }
    MeasurementDialog(
        isOpen = isAddItemDialogOpen,
        title = "Add/Update Body Part",
        body = {
            OutlinedTextField(
                value = state.textFieldValue,
                onValueChange = {
                    onEvent(AddItemEvent.OnTextValueChange(it))
                }
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = "update icon"
            )
        },
        onConfirmButtonClick = {
            onEvent(AddItemEvent.UpsertItem)
            isAddItemDialogOpen = false
        },
        onDismissButtonClick = {
            onEvent(AddItemEvent.OnAddItemDialogDismiss)
            isAddItemDialogOpen = false
        },
        confirmTextButton = "save"
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        AddItemTopBar(
            onBackIconClick = { onBackIconClick() },
            onAddIconClick = { isAddItemDialogOpen = true }
        )

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            items(state.bodyPart){ bodyPart ->
                AddItemCard(
                    name = bodyPart.name,
                    isChecked = bodyPart.isActive,
                    onCheckedChanged = {
                        onEvent(AddItemEvent.OnItemIsActiveChange(bodyPart))
                    },
                    onCardClick = {
                        isAddItemDialogOpen = true
                        onEvent(AddItemEvent.OnItemClick(bodyPart))
                    }
                )
            }

        }

    }



}

