package com.moonwalkin.numbertesttask.domain

import kotlinx.coroutines.flow.Flow

interface NumberRepository {
    fun getNumbersHistory(): Flow<Result<List<NumberInfo>>>
    suspend fun getNumberInfo(number: Long): Result<NumberInfo>
    suspend fun getRandomNumberInfo(): Result<NumberInfo>
}