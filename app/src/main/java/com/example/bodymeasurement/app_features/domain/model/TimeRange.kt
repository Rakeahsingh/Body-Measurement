package com.example.bodymeasurement.app_features.domain.model

enum class TimeRange(
    val label: String
) {

    LAST7DAYS("Last 7 days"),
    LAST30DAYS("Last 30 days"),
    ALL_TIME("All Time")

}