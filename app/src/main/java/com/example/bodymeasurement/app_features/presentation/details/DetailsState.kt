package com.example.bodymeasurement.app_features.presentation.details

import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.app_features.domain.model.TimeRange
import java.time.LocalDate

data class DetailsState(
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentDeletedBodyPartValue: BodyPartsValue? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST7DAYS,
    val allBodyPartValues: List<BodyPartsValue> = emptyList(),
    val graphBodyPartValues: List<BodyPartsValue> = emptyList()
)
