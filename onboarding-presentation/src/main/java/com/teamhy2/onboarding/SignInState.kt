package com.teamhy2.onboarding

sealed interface SignInState {
    data object Idle : SignInState

    data object Success : SignInState

    data object Failure : SignInState
}
