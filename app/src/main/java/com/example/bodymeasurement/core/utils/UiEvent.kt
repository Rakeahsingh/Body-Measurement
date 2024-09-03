package com.example.bodymeasurement.core.utils

sealed class UiEvent {

    data class SnackBar(
        val message: String,
        val actionLabel: String? = null
    ): UiEvent()

    data object HideBottomSheet: UiEvent()

    data object Navigate: UiEvent()

}