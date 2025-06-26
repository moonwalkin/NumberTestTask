package com.moonwalkin.numbertesttask

import com.moonwalkin.data.network.NumberInfoDto
import com.moonwalkin.data.network.NumberService


class FakeNumberService : NumberService {
    val numberResponses = mutableMapOf<Long, NumberInfoDto>()
    var randomResponse: NumberInfoDto = NumberInfoDto(number = 0, text = "Default Random")

    override suspend fun getNumberInfo(number: Long): NumberInfoDto {
        return numberResponses[number] ?: error("Number not found")
    }

    override suspend fun getRandomNumberInfo(): NumberInfoDto {
        return randomResponse
    }
}