package com.teamhy2.onboarding

data object OnboardingState

sealed interface OnboardingSideEffect {
    data class ShowError(val errorMessage: String?) : OnboardingSideEffect

    sealed interface Navigation : OnboardingSideEffect {
        data object SignIn : Navigation

        data object Home : Navigation
    }
}
