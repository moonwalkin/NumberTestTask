package com.moonwalkin.numbertesttask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberDao {
     @Insert
     suspend fun insertNumberInfo(numberInfo: NumberInfoEntity)

     @Query("SELECT * FROM numberInfo ORDER BY id DESC")
     fun getSearchHistory(): Flow<List<NumberInfoEntity>>
}