package com.moonwalkin.domain

import me.tatarka.inject.annotations.Inject

class GetRandomNumberUseCase @Inject constructor(private val repository: NumberRepository) {
    suspend operator fun invoke(): Result<NumberInfo> {
        return repository.getRandomNumberInfo()
    }
}