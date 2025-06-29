package com.teamhy2.designsystem.util.mvi

sealed interface MviContract {
    interface UiIntent : MviContract

    interface SideEffect : MviContract

    interface UiState : MviContract
}
