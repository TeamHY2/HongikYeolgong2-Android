package com.teamhy2.designsystem.util.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class MviViewModel<INTENT : UiIntent, STATE : UiState, SIDE_EFFECT : SideEffect>(
    initialState: STATE,
) : ViewModel() {
    private val intents = Channel<INTENT>()

    val uiState: StateFlow<STATE> =
        intents.receiveAsFlow()
            .runningFold(initialState, ::reduceState)
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)

    private val _sideEffect: Channel<SIDE_EFFECT> = Channel(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SIDE_EFFECT> = _sideEffect.receiveAsFlow()

    protected abstract suspend fun reduceState(
        current: STATE,
        intent: INTENT,
    ): STATE

    fun sendIntent(intent: INTENT) {
        viewModelScope.launch {
            intents.send(intent)
        }
    }

    protected fun postSideEffect(vararg effects: SIDE_EFFECT) {
        viewModelScope.launch {
            effects.forEach { effect ->
                _sideEffect.send(effect)
            }
        }
    }

    protected inline fun <reified T : STATE> runOn(
        current: STATE,
        block: T.() -> STATE,
    ): STATE {
        return (current as? T)?.run(block) ?: current
    }
}
