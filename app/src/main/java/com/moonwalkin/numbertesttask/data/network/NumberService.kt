package com.moonwalkin.numbertesttask.data.network

import com.moonwalkin.numbertesttask.data.NumberInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface NumberService {
    @GET("/{number}?json")
    suspend fun getNumberInfo(@Path("number") number: Int): NumberInfo

    @GET("/random/math?json")
    suspend fun getRandomNumberInfo(): NumberInfo
}