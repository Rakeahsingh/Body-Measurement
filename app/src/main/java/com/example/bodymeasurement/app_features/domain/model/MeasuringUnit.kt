package com.example.bodymeasurement.app_features.domain.model

enum class MeasuringUnit(
    val code: String,
    val label: String
) {

    CM("cm", "centimeters"),
    M("m", "meters"),
    IN("in", "inches"),
    FT("ft", "feet"),
    PERCENT("%", "percentage"),
    KG("kg", "kilograms"),
    PD("pd", "pounds"),
    MM("mm", "millimeters")


}