package com.moonwalkin.numbertesttask.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface NumberService {
    @GET("/{number}?json")
    suspend fun getNumberInfo(@Path("number") number: Long): NumberInfoDto

    @GET("/random/math?json")
    suspend fun getRandomNumberInfo(): NumberInfoDto
}