package com.moonwalkin.numbertesttask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonwalkin.numbertesttask.data.NumberInfo

@Dao
interface NumberDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertNumberInfo(numberInfo: NumberInfo)

     @Query("SELECT * FROM numberInfo")
     suspend fun getSearchHistory(): List<NumberInfo>
}