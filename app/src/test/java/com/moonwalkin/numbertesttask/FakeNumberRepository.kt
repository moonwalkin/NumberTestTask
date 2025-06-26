package com.moonwalkin.numbertesttask

import com.moonwalkin.domain.NumberInfo
import com.moonwalkin.domain.NumberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNumberRepository : NumberRepository {
    var historyFlow: Flow<Result<List<NumberInfo>>> = flowOf(Result.success(emptyList()))
    var infoResult: Result<NumberInfo> = Result.success(NumberInfo(id = 0, number = 0, text = ""))
    var randomResult: Result<NumberInfo> = Result.success(NumberInfo(id = 0, number = 0, text = "Random"))

    override fun getNumbersHistory(): Flow<Result<List<NumberInfo>>> = historyFlow

    override suspend fun getNumberInfo(number: Long): Result<NumberInfo> = infoResult

    override suspend fun getRandomNumberInfo(): Result<NumberInfo> = randomResult
}