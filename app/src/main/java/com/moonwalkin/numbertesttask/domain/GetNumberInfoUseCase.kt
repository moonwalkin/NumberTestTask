package com.moonwalkin.numbertesttask.domain

class GetNumberInfoUseCase(private val repository: NumberRepository) {
    suspend operator fun invoke(number: Int): Result<NumberInfo> {
        return repository.getNumberInfo(number)
    }
}