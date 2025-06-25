package com.moonwalkin.numbertesttask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NumberDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertNumberInfo(numberInfo: NumberInfoEntity)

     @Query("SELECT * FROM numberInfo")
     suspend fun getSearchHistory(): List<NumberInfoEntity>

     @Query("SELECT * FROM numberInfo WHERE number = :number")
     suspend fun getNumberInfo(number: Int): NumberInfoEntity?
}