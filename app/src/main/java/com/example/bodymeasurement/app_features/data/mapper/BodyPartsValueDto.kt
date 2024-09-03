package com.example.bodymeasurement.app_features.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class BodyPartsValueDto(
    val value: Float = 0.05f,
    val date: Timestamp = Timestamp.now(),
    val bodyPartId: String? = null,
    val bodyPartValueId: String? = null
)


@RequiresApi(Build.VERSION_CODES.O)
fun BodyPartsValueDto.toBodyPartValue(): BodyPartsValue{
    return BodyPartsValue(
        value = value,
        date = date.toLocalDate(),
        bodyPartId = bodyPartId,
        bodyPartValueId = bodyPartValueId
    )
}


@RequiresApi(Build.VERSION_CODES.O)
fun BodyPartsValue.toBodyPartValueDto(): BodyPartsValueDto{
    return BodyPartsValueDto(
        value = value,
        date = date.toTimestamp(),
        bodyPartId = bodyPartId,
        bodyPartValueId = bodyPartValueId
    )
}





@RequiresApi(Build.VERSION_CODES.O)
private fun Timestamp.toLocalDate() : LocalDate{
    return Instant
        .ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}


@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDate.toTimestamp(): Timestamp{
    val instant: Instant = this
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()

    return Timestamp(instant.toEpochMilli() / 1000, instant.nano % 1000000)
}
