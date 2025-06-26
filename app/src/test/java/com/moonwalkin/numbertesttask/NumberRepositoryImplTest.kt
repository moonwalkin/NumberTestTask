package com.moonwalkin.numbertesttask

import com.moonwalkin.numbertesttask.data.database.toEntity
import com.moonwalkin.numbertesttask.data.network.NumberInfoDto
import com.moonwalkin.numbertesttask.data.network.NumberService
import com.moonwalkin.numbertesttask.data.network.toDomain
import com.moonwalkin.numbertesttask.data.network.toEntity
import com.moonwalkin.numbertesttask.data.repository.NumberRepositoryImpl
import com.moonwalkin.numbertesttask.domain.NumberInfo
import com.moonwalkin.numbertesttask.domain.NumberRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class NumberRepositoryImplTest {

    private lateinit var fakeDao: FakeNumberDao
    private lateinit var fakeService: FakeNumberService
    private lateinit var repository: NumberRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        fakeDao = FakeNumberDao()
        fakeService = FakeNumberService()
        repository = NumberRepositoryImpl(fakeDao, fakeService, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getNumbersHistory returns previously saved items`() = runTest {
        val domain = NumberInfo(1, "One", 2)
        fakeDao.insertNumberInfo(domain.toEntity())

        val result = repository.getNumbersHistory().first()
        assertTrue(result.isSuccess)
        assertEquals(listOf(domain), result.getOrNull())
    }

    @Test
    fun `getNumberInfo fetches from service and stores in dao`() = runTest {
        val number = 42L
        fakeService.numberResponses[number] = NumberInfoDto(number = number, text = "Answer to Everything")

        val result = repository.getNumberInfo(number)

        assertTrue(result.isSuccess)
        val expected = NumberInfo(id = 0, number = 42, text = "Answer to Everything")
        assertEquals(expected, result.getOrNull())
        assertTrue(fakeDao.storedEntities.contains(expected.toEntity()))
    }

    @Test
    fun `getRandomNumberInfo fetches random and stores in dao`() = runTest {
        val dto = NumberInfoDto(number = 7, text = "Lucky Seven")
        fakeService.randomResponse = dto

        val result = repository.getRandomNumberInfo()

        assertTrue(result.isSuccess)
        val expected = NumberInfoDto(number = 7, text = "Lucky Seven")
        assertEquals(expected.toDomain(), result.getOrNull())
        assertTrue(fakeDao.storedEntities.contains(expected.toEntity()))
    }

    @Test
    fun `getNumbersHistory handles exception`() = runTest {
        fakeDao.shouldThrow = true

        val result = repository.getNumbersHistory().first()
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `getNumberInfo returns failure when service throws exception`() = runTest {
        val number = 99L

        val result = repository.getNumberInfo(number)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `getRandomNumberInfo calls service once`() = runTest {
        val dto = NumberInfoDto(number = 5, text = "Five")
        fakeService.randomResponse = dto

        var callCount = 0
        val spyingService = object : NumberService {
            override suspend fun getNumberInfo(number: Long): NumberInfoDto {
                error("Not used")
            }

            override suspend fun getRandomNumberInfo(): NumberInfoDto {
                callCount++
                return dto
            }
        }
        repository = NumberRepositoryImpl(fakeDao, spyingService, testDispatcher)

        repository.getRandomNumberInfo()
        repository.getRandomNumberInfo()

        assertEquals(2, callCount)
    }

    @Test
    fun `getNumbersHistory returns multiple saved items`() = runTest {
        val domain1 = NumberInfo(1, "One", 1)
        val domain2 = NumberInfo(2, "Two", 2)
        fakeDao.insertNumberInfo(domain1.toEntity())
        fakeDao.insertNumberInfo(domain2.toEntity())

        val result = repository.getNumbersHistory().first()

        assertTrue(result.isSuccess)
        assertEquals(listOf(domain1, domain2), result.getOrNull())
    }

    @Test
    fun `getNumberInfo rethrows CancellationException`() = runTest {
        val cancellingService = object : NumberService {
            override suspend fun getNumberInfo(number: Long): NumberInfoDto {
                throw CancellationException("Test cancellation")
            }

            override suspend fun getRandomNumberInfo(): NumberInfoDto {
                error("Should not be called")
            }
        }

        repository = NumberRepositoryImpl(fakeDao, cancellingService, testDispatcher)

        try {
            repository.getNumberInfo(123)
            fail("Expected CancellationException to be thrown")
        } catch (e: CancellationException) {
            assertEquals("Test cancellation", e.message)
        }
    }
}
