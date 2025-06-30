package com.moonwalkin.numbertesttask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moonwalkin.numbertesttask.data.database.NumberDao
import com.moonwalkin.numbertesttask.data.database.NumberDatabase
import com.moonwalkin.numbertesttask.data.database.NumberInfoEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var numberDao: NumberDao
    private lateinit var db: NumberDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NumberDatabase::class.java
        ).build()
        numberDao = db.numberDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun checkThatSizeAfterInsertIsOne() = runTest {
        val numberFact = NumberInfoEntity(
            number = 42,
            text = "The answer to life, the universe, and everything",
            id = 0,
        )
        numberDao.insertNumberInfo(numberFact)
        assertEquals(
            numberDao.getSearchHistory().first().size,
            1
        )
    }

    @Test
    fun addTwoAndCheckLastAdded() = runTest {
        val numberFact1 = NumberInfoEntity(
            number = 42,
            text = "The answer to life, the universe, and everything",
            id = 5,
        )
        val numberFact2 = NumberInfoEntity(
            number = 7,
            text = "Lucky number seven",
            id = 6
        )
        numberDao.insertNumberInfo(numberFact1)
        numberDao.insertNumberInfo(numberFact2)

        val history = numberDao.getSearchHistory().first()
        assertEquals(history.size, 2)
        assertEquals(history.last(), numberFact1)
    }

    @Test
    fun addTwoAndCheckAll() = runTest {
        val numberFact1 = NumberInfoEntity(
            number = 42,
            text = "The answer to life, the universe, and everything",
            id = 5,
        )
        val numberFact2 = NumberInfoEntity(
            number = 7,
            text = "Lucky number seven",
            id = 6
        )
        val numberFact3 = NumberInfoEntity(
            number = 3,
            text = "Lucky number three",
            id = 9
        )
        numberDao.insertNumberInfo(numberFact1)
        numberDao.insertNumberInfo(numberFact2)
        numberDao.insertNumberInfo(numberFact3)

        val history = numberDao.getSearchHistory().first()
        assertEquals(history.size, 3)
        assertEquals(history, listOf(numberFact3, numberFact2, numberFact1))
    }
}