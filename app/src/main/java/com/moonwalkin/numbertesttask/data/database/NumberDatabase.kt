package com.moonwalkin.numbertesttask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moonwalkin.numbertesttask.data.network.NumberInfo

@Database(entities = [NumberInfoEntity::class], version = 1)
abstract class NumberDatabase : RoomDatabase() {
    abstract fun numberDao(): NumberDao
}