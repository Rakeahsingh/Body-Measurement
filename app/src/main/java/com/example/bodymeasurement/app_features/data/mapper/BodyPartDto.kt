package com.example.bodymeasurement.app_features.data.mapper

import com.example.bodymeasurement.app_features.domain.model.BodyPart

data class BodyPartDto(
    val name: String = "",
    val active: Boolean = true,
    val measuringUnit: String = "",
    val latestValue: Float? = null,
    val bodyPartId: String? = null
)

fun BodyPart.toBodyPartDto(): BodyPartDto{
    return BodyPartDto(
        name = name,
        active = isActive,
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPartId = bodyPartId
    )
}

fun BodyPartDto.toBodyPart(): BodyPart{
    return BodyPart(
        name = name,
        isActive = active,
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPartId = bodyPartId
    )
}
