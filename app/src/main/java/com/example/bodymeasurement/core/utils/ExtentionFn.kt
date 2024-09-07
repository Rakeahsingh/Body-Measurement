package com.example.bodymeasurement.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}

fun String.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    val value = this.toFloatOrNull() ?: 0f
    return (value * multiplier).roundToInt() / multiplier.toFloat()
}

fun LocalDate?.changeLocalDateToGraphDate(
    defaultValue: LocalDate = LocalDate.now()
): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("MMM dd"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}

fun LocalDate?.changeLocalDateToDateString(
    defaultValue: LocalDate = LocalDate.now()
): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}

fun Long?.changeMillisToLocalDate(): LocalDate {
    return try {
        this?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } ?: LocalDate.now()
    } catch (e: Exception) {
        LocalDate.now()
    }
}