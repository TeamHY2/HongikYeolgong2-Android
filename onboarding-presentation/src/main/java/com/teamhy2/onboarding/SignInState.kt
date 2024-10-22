package com.teamhy2.onboarding

sealed interface SignInState {
    data object Idle : SignInState

    data object SuccessfulSignedInGuest : SignInState

    data object SuccessfulSignedInUser : SignInState

    data object Failure : SignInState
}
