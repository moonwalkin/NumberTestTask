package com.moonwalkin.domain

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class GetNumbersHistoryUseCase @Inject constructor(private val repository: NumberRepository) {
    operator fun invoke(): Flow<Result<List<NumberInfo>>> {
        return repository.getNumbersHistory()
    }
}