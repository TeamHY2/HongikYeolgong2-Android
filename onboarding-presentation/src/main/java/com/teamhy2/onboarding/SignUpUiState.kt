package com.teamhy2.onboarding

data class SignUpUiState(
    val departments: List<String>,
    val isNicknameValidate: Boolean,
    val nicknameState: NicknameState,
    val isDepartmentValidate: Boolean,
)
