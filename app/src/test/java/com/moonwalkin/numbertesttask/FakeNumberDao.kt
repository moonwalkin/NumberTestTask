package com.moonwalkin.numbertesttask

import com.moonwalkin.numbertesttask.data.database.NumberDao
import com.moonwalkin.numbertesttask.data.database.NumberInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNumberDao : NumberDao {
    val storedEntities = mutableListOf<NumberInfoEntity>()
    var shouldThrow = false

    override fun getSearchHistory(): Flow<List<NumberInfoEntity>> = flow {
        if (shouldThrow) throw RuntimeException("DB Error")
        emit(storedEntities)
    }

    override suspend fun insertNumberInfo(numberInfo: NumberInfoEntity) {
        storedEntities.add(numberInfo)
    }
}