package com.example.bodymeasurement.app_features.presentation.details

import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.app_features.domain.model.MeasuringUnit
import com.example.bodymeasurement.app_features.domain.model.TimeRange

sealed class DetailsEvent {

    data object DeleteBodyPart: DetailsEvent()
    data object RestoreBodyPartValue: DetailsEvent()
    data object AddNewValue: DetailsEvent()

    data class DeleteBodyPartValue(val bodyPartsValue: BodyPartsValue): DetailsEvent()
    data class ChangeMeasuringUnit(val measuringUnit: MeasuringUnit): DetailsEvent()
    data class OnDateChange(val millis: Long?): DetailsEvent()
    data class OnTextFieldValueChange(val value: String): DetailsEvent()
    data class OnTimeRangeChange(val timeRange: TimeRange): DetailsEvent()

}