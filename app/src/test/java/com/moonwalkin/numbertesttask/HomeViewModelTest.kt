package com.moonwalkin.numbertesttask

import com.moonwalkin.numbertesttask.domain.GetNumberInfoUseCase
import com.moonwalkin.numbertesttask.domain.GetNumbersHistoryUseCase
import com.moonwalkin.numbertesttask.domain.GetRandomNumberUseCase
import com.moonwalkin.numbertesttask.domain.NumberInfo
import com.moonwalkin.numbertesttask.presentation.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeNumberRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        fakeRepository = FakeNumberRepository()
    }

    @Test
    fun `initial state loads history successfully`() = runTest {
        val history = listOf(NumberInfo(1, "One", 1), NumberInfo(2, "Two", 2))
        fakeRepository.historyFlow = flowOf(Result.success(history))

        viewModel = HomeViewModel(
            GetNumberInfoUseCase(fakeRepository),
            GetRandomNumberUseCase(fakeRepository),
            GetNumbersHistoryUseCase(fakeRepository)
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(history, state.history)
        assertNull(state.error)
    }

    @Test
    fun `initial state sets error when history fails`() = runTest {
        fakeRepository.historyFlow = flowOf(Result.failure(RuntimeException("DB error")))

        viewModel = HomeViewModel(
            GetNumberInfoUseCase(fakeRepository),
            GetRandomNumberUseCase(fakeRepository),
            GetNumbersHistoryUseCase(fakeRepository)
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.history.isEmpty())
        assertEquals("DB error", state.error)
    }

    @Test
    fun `loadNumberInfo updates state on success`() = runTest {
        val info = NumberInfo(id = 0, number = 5, text = "Five")
        fakeRepository.historyFlow = flowOf(Result.success(emptyList()))
        fakeRepository.infoResult = Result.success(info)

        viewModel = HomeViewModel(
            GetNumberInfoUseCase(fakeRepository),
            GetRandomNumberUseCase(fakeRepository),
            GetNumbersHistoryUseCase(fakeRepository)
        )

        viewModel.loadNumberInfo(5)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Five", state.numberInfo)
        assertNull(state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loadNumberInfo updates error on failure`() = runTest {
        fakeRepository.historyFlow = flowOf(Result.success(emptyList()))
        fakeRepository.infoResult = Result.failure(RuntimeException("Network error"))

        viewModel = HomeViewModel(
            GetNumberInfoUseCase(fakeRepository),
            GetRandomNumberUseCase(fakeRepository),
            GetNumbersHistoryUseCase(fakeRepository)
        )

        viewModel.loadNumberInfo(10)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Network error", state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `getRandomNumberInfo sets random number info`() = runTest {
        val randomInfo = NumberInfo(id = 0, number = 7, text = "Lucky Seven")
        fakeRepository.historyFlow = flowOf(Result.success(emptyList()))
        fakeRepository.randomResult = Result.success(randomInfo)

        viewModel = HomeViewModel(
            GetNumberInfoUseCase(fakeRepository),
            GetRandomNumberUseCase(fakeRepository),
            GetNumbersHistoryUseCase(fakeRepository)
        )

        viewModel.getRandomNumberInfo()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Lucky Seven", state.numberInfo)
        assertNull(state.error)
    }
}