package com.example.bodymeasurement.app_features.presentation.addItem

import com.example.bodymeasurement.app_features.domain.model.BodyPart

sealed class AddItemEvent {

    data class OnTextValueChange(val value: String): AddItemEvent()
    data class OnItemClick(val bodyPart: BodyPart): AddItemEvent()
    data class OnItemIsActiveChange(val bodyPart: BodyPart): AddItemEvent()
    data object OnAddItemDialogDismiss: AddItemEvent()
    data object UpsertItem: AddItemEvent()


}