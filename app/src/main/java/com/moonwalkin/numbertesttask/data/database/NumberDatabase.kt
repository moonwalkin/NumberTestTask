package com.moonwalkin.numbertesttask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moonwalkin.numbertesttask.data.NumberInfo

@Database(entities = [NumberInfo::class], version = 1)
abstract class NumberDatabase : RoomDatabase() {
    abstract fun numberDao(): NumberDao
}