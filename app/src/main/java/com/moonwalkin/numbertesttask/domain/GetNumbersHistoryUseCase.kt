package com.moonwalkin.numbertesttask.domain

class GetNumbersHistoryUseCase(private val repository: NumberRepository) {
    suspend operator fun invoke(): Result<List<NumberInfo>> {
        return repository.getNumbersHistory()
    }
}