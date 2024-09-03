package com.example.bodymeasurement.app_features.domain.model

data class BodyPart(
    val name: String,
    val isActive: Boolean,
    val measuringUnit: String,
    val latestValue: Float? = null,
    val bodyPartId: String? = null
)

val predefinedBodyParts: List<BodyPart> = listOf(

    BodyPart(
        "Waist",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Body Fat",
        true,
        MeasuringUnit.PERCENT.code
    ),

    BodyPart(
        "Height",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Weight",
        true,
        MeasuringUnit.KG.code
    ),

    BodyPart(
        "Biceps (Left)",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Biceps (Right)",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Chest",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Triceps (Left)",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        "Triceps (Right)",
        true,
        MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Shoulders",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Hips",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Thigh (Left)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Thigh (Right)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Calve (Left)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),

    BodyPart(
        name = "Calve (Right)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    )


)
