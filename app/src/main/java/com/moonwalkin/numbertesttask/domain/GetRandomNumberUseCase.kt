package com.moonwalkin.numbertesttask.domain

class GetRandomNumberUseCase(private val repository: NumberRepository) {
    suspend operator fun invoke(): Result<NumberInfo> {
        return repository.getRandomNumberInfo()
    }
}