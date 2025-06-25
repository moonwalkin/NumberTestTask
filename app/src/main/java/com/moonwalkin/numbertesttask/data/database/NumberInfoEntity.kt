package com.moonwalkin.numbertesttask.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonwalkin.numbertesttask.domain.NumberInfo

@Entity(tableName = "numberInfo")
class NumberInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val number: Int
)

fun NumberInfoEntity.toDomain(): NumberInfo {
    return NumberInfo(
        text = text,
        number = number
    )
}

fun NumberInfo.toEntity(): NumberInfoEntity {
    return NumberInfoEntity(
        text = text,
        number = number
    )
}