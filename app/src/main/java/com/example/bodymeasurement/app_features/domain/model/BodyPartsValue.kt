package com.example.bodymeasurement.app_features.domain.model

import java.time.LocalDate

data class BodyPartsValue(
    val value: Float,
    val date: LocalDate,
    val bodyPartId: String? = null,
    val bodyPartValueId: String? = null
)
