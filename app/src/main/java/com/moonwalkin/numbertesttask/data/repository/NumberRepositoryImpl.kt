package com.moonwalkin.numbertesttask.data.repository

import com.moonwalkin.numbertesttask.data.database.NumberDao
import com.moonwalkin.numbertesttask.data.database.NumberInfoEntity
import com.moonwalkin.numbertesttask.data.database.toDomain
import com.moonwalkin.numbertesttask.data.database.toEntity
import com.moonwalkin.numbertesttask.data.network.NumberService
import com.moonwalkin.numbertesttask.data.network.toDomain
import com.moonwalkin.numbertesttask.data.network.toEntity
import com.moonwalkin.numbertesttask.di.IoDispatcher
import com.moonwalkin.numbertesttask.domain.NumberInfo
import com.moonwalkin.numbertesttask.domain.NumberRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.runCatching

class NumberRepositoryImpl @Inject constructor(
    private val numberDao: NumberDao,
    private val numberService: NumberService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : NumberRepository {

    override fun getNumbersHistory(): Flow<Result<List<NumberInfo>>> {
        return numberDao.getSearchHistory()
            .map { entities ->
                Result.success(entities.map(NumberInfoEntity::toDomain))
            }
            .catch {
                Result.failure<Exception>(it)
            }
    }

    override suspend fun getNumberInfo(number: Long): Result<NumberInfo> {
        return runCatching {
            withContext(dispatcher) {
                numberService.getNumberInfo(number).toDomain()
                    .also {
                        numberDao.insertNumberInfo(it.toEntity())
                    }
            }
        }
    }

    override suspend fun getRandomNumberInfo(): Result<NumberInfo> {
        return runCatching {
            withContext(dispatcher) {
                numberService.getRandomNumberInfo()
                    .also { numberDao.insertNumberInfo(it.toEntity()) }
                    .toDomain()
            }
        }
    }
}