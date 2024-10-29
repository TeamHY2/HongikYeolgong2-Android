package com.teamhy2.onboarding

sealed interface SignUpUiState {
    data object Loading : SignUpUiState

    data class Success(
        val departments: List<String>,
        val isNicknameValidate: Boolean,
        val nicknameState: NicknameState,
        val isDepartmentValidate: Boolean,
    ) : SignUpUiState

    data object SignUpDone : SignUpUiState
}
