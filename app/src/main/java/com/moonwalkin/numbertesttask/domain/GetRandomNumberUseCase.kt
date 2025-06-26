package com.moonwalkin.numbertesttask.domain

import javax.inject.Inject

class GetRandomNumberUseCase @Inject constructor(private val repository: NumberRepository) {
    suspend operator fun invoke(): Result<NumberInfo> {
        return repository.getRandomNumberInfo()
    }
}