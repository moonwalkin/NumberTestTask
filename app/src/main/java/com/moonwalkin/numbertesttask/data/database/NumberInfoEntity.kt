package com.moonwalkin.numbertesttask.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonwalkin.numbertesttask.domain.NumberInfo

@Entity(tableName = "numberInfo")
class NumberInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val number: Long
)

fun NumberInfoEntity.toDomain(): NumberInfo {
    return NumberInfo(
        text = text,
        number = number,
        id = id
    )
}

fun NumberInfo.toEntity(): NumberInfoEntity {
    return NumberInfoEntity(
        id = id,
        text = text,
        number = number
    )
}