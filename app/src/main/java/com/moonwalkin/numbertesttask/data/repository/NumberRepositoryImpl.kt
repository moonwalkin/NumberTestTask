package com.moonwalkin.numbertesttask.data.repository

import com.moonwalkin.numbertesttask.data.database.NumberDao
import com.moonwalkin.numbertesttask.data.database.NumberInfoEntity
import com.moonwalkin.numbertesttask.data.database.toDomain
import com.moonwalkin.numbertesttask.data.database.toEntity
import com.moonwalkin.numbertesttask.data.network.NumberService
import com.moonwalkin.numbertesttask.data.network.toDomain
import com.moonwalkin.numbertesttask.domain.NumberInfo
import com.moonwalkin.numbertesttask.domain.NumberRepository
import kotlin.runCatching

class NumberRepositoryImpl(
    private val numberDao: NumberDao,
    private val numberService: NumberService
) : NumberRepository {

    override suspend fun getNumbersHistory(): Result<List<NumberInfo>> {
        return runCatching {
            numberDao.getSearchHistory().map(NumberInfoEntity::toDomain)
        }
    }

    override suspend fun getNumberInfo(number: Int): Result<NumberInfo> {
        return runCatching {
            val cachedEntity = numberDao.getNumberInfo(number)
            val numberInfo = cachedEntity?.toDomain() ?: numberService.getNumberInfo(number).toDomain()
            numberDao.insertNumberInfo(numberInfo.toEntity())
            numberInfo
        }
    }

    override suspend fun getRandomNumberInfo(): Result<NumberInfo> {
        return runCatching {
            numberService.getRandomNumberInfo().toDomain()
        }
    }
}