package com.moonwalkin.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwalkin.domain.GetNumberInfoUseCase
import com.moonwalkin.domain.GetNumbersHistoryUseCase
import com.moonwalkin.domain.GetRandomNumberUseCase
import com.moonwalkin.domain.NumberInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNumberInfoUseCase: GetNumberInfoUseCase,
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
    getNumbersHistoryUseCase: GetNumbersHistoryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getNumbersHistoryUseCase().collect { history ->
                _state.update { state ->
                    history
                        .map { state.copy(history = it, error = null) }
                        .getOrElse { state.copy(error = it.message) }
                }
            }
        }
    }

    fun loadNumberInfo(number: Long) {
        viewModelScope.launch {
            loadNumberInfo {
                getNumberInfoUseCase(number)
            }
        }
    }

    fun getRandomNumberInfo() {
        viewModelScope.launch {
            loadNumberInfo {
                getRandomNumberUseCase()
            }
        }
    }

    private suspend fun loadNumberInfo(action: suspend () -> Result<NumberInfo>) {
        _state.update { state ->
            state.copy(isLoading = true, error = null)
        }
        action()
            .onSuccess { numberInfo ->
                _state.update { state ->
                    state.copy(
                        numberInfo = numberInfo.text,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .onFailure { error ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
    }

    @Immutable
    data class HomeScreenState(
        val numberInfo: String = "",
        val history: List<NumberInfo> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}