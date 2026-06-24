package com.teamhy2.onboarding

data object OnboardingState

sealed interface OnboardingSideEffect {
    data class ShowError(val errorMessage: String?) : OnboardingSideEffect

    data object NavigateToSignIn : OnboardingSideEffect

    data object NavigateToHome : OnboardingSideEffect
}
