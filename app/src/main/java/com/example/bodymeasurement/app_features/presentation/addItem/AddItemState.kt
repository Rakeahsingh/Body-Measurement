package com.example.bodymeasurement.app_features.presentation.addItem

import com.example.bodymeasurement.app_features.domain.model.BodyPart

data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyPart: List<BodyPart> = emptyList()
)
