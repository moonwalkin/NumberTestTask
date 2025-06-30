package com.moonwalkin.numbertesttask.presentation.home

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.moonwalkin.numbertesttask.di.MainDispatcher
import com.moonwalkin.numbertesttask.domain.GetNumberInfoUseCase
import com.moonwalkin.numbertesttask.domain.GetNumbersHistoryUseCase
import com.moonwalkin.numbertesttask.domain.GetRandomNumberUseCase
import com.moonwalkin.numbertesttask.domain.NumberInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DefaultHomeComponent @AssistedInject constructor(
    @Assisted("componentContext") private val componentContext: ComponentContext,
    private val getNumberInfoUseCase: GetNumberInfoUseCase,
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
    getNumbersHistoryUseCase: GetNumbersHistoryUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @Assisted("onItemClicked") private val onItemClicked: (Long, String) -> Unit
) : HomeComponent {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _model = MutableValue(HomeScreenState())
    override val model: Value<HomeScreenState> = _model

    init {
        componentContext.lifecycle.doOnDestroy {
            scope.cancel()
        }
        scope.launch(mainDispatcher) {
            getNumbersHistoryUseCase().collect { history ->
                _model.update { state ->
                    history
                        .map { state.copy(history = it, error = null) }
                        .getOrElse { state.copy(error = it.message) }
                }
            }
        }
    }

    override fun loadNumberInfo(number: Long) {
        scope.launch(mainDispatcher) {
            loadNumberInfo {
                getNumberInfoUseCase(number)
            }
        }
    }

    override fun loadRandomNumberInfo() {
        scope.launch(mainDispatcher) {
            loadNumberInfo {
                getRandomNumberUseCase()
            }
        }
    }

    override fun onItemSelected(number: Long, text: String) {
        onItemClicked(number, text)
    }

    private suspend fun loadNumberInfo(action: suspend () -> Result<NumberInfo>) {
        _model.update { state ->
            state.copy(isLoading = true, error = null)
        }
        action()
            .onSuccess { numberInfo ->
                _model.update { state ->
                    state.copy(
                        numberInfo = numberInfo.text,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .onFailure { error ->
                _model.update { state ->
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onItemClicked") onItemSelected: (Long, String) -> Unit
        ): DefaultHomeComponent
    }
}