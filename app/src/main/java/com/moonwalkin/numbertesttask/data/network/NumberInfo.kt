package com.moonwalkin.numbertesttask.data.network

import com.squareup.moshi.Json

data class NumberInfo(
    @Json(name = "text") val text: String,
    @Json(name = "number") val number: Int
)