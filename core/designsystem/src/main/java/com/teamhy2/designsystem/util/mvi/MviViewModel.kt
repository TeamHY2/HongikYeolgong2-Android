package com.teamhy2.designsystem.util.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<INTENT : MviContract.UiIntent, STATE : MviContract.UiState, SIDE_EFFECT : MviContract.SideEffect>(
    initialState: STATE,
) : ViewModel() {
    private val _uiState: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    protected val state: STATE
        get() = _uiState.value

    private val _sideEffect: Channel<SIDE_EFFECT> = Channel(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SIDE_EFFECT> = _sideEffect.receiveAsFlow()

    private val intents = Channel<INTENT>()

    init {
        intents.receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    protected abstract fun handleIntent(intent: INTENT)

    fun sendIntent(intent: INTENT) {
        viewModelScope.launch {
            intents.send(intent)
        }
    }

    protected fun reduce(block: STATE.() -> STATE) {
        _uiState.update(block)
    }

    protected fun postSideEffect(vararg effects: SIDE_EFFECT) {
        viewModelScope.launch {
            effects.forEach { effect ->
                _sideEffect.send(effect)
            }
        }
    }

    protected inline fun <reified T : STATE> runOn(block: (T) -> Unit) {
        (state as? T)?.let { block(it) }
    }
}
