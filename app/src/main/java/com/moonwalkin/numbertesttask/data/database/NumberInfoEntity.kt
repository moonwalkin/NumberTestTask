package com.moonwalkin.numbertesttask.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "numberInfo")
data class NumberInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val number: Int
)