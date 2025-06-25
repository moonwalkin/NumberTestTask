package com.moonwalkin.numbertesttask.domain

interface NumberRepository {
    suspend fun getNumbersHistory(): Result<List<NumberInfo>>
    suspend fun getNumberInfo(number: Int): Result<NumberInfo>
    suspend fun getRandomNumberInfo(): Result<NumberInfo>
}