package com.moonwalkin.numbertesttask.data.network

import com.moonwalkin.numbertesttask.data.database.NumberInfoEntity
import com.moonwalkin.numbertesttask.domain.NumberInfo
import com.squareup.moshi.Json

class NumberInfoDto(
    @Json(name = "text") val text: String,
    @Json(name = "number") val number: Int
)

fun NumberInfoDto.toDomain(): NumberInfo {
    return NumberInfo(
        text = text,
        number = number
    )
}

fun NumberInfoDto.toEntity(): NumberInfoEntity {
    return NumberInfoEntity(
        text = text,
        number = number
    )
}