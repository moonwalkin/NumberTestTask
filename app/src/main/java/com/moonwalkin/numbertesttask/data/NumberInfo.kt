package com.moonwalkin.numbertesttask.data

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "numberInfo")
data class NumberInfo(
    @Json(name = "text") val text: String,
    @Json(name = "number") val number: Int
)