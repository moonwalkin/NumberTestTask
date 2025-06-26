package com.moonwalkin.domain

import me.tatarka.inject.annotations.Inject

class GetNumberInfoUseCase @Inject constructor(private val repository: NumberRepository) {
    suspend operator fun invoke(number: Long): Result<NumberInfo> {
        return repository.getNumberInfo(number)
    }
}