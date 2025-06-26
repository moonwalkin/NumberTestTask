package com.moonwalkin.data.repository

import com.moonwalkin.data.database.NumberDao
import com.moonwalkin.data.database.NumberInfoEntity
import com.moonwalkin.data.database.toDomain
import com.moonwalkin.data.database.toEntity
import com.moonwalkin.data.network.NumberService
import com.moonwalkin.data.network.toDomain
import com.moonwalkin.data.network.toEntity
import com.moonwalkin.domain.IoDispatcher
import com.moonwalkin.domain.NumberInfo
import com.moonwalkin.domain.NumberRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.cancellation.CancellationException

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
                emit(Result.failure(it))
            }
    }

    override suspend fun getNumberInfo(number: Long): Result<NumberInfo> {
        return withContext(dispatcher) {
            safeCall {
                numberService.getNumberInfo(number).toDomain()
                    .also {
                        numberDao.insertNumberInfo(it.toEntity())
                    }
            }
        }
    }

    override suspend fun getRandomNumberInfo(): Result<NumberInfo> {
        return withContext(dispatcher) {
            safeCall {
                numberService.getRandomNumberInfo()
                    .also { numberDao.insertNumberInfo(it.toEntity()) }
                    .toDomain()
            }
        }
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}