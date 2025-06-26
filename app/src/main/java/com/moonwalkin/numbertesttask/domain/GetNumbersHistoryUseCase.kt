package com.moonwalkin.numbertesttask.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNumbersHistoryUseCase @Inject constructor(private val repository: NumberRepository) {
    operator fun invoke(): Flow<Result<List<NumberInfo>>> {
        return repository.getNumbersHistory()
    }
}