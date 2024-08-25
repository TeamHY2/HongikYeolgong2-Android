package com.teamhy2.onboarding

data class SignUpUiState(
    val departments: List<String>,
    val isNicknameValidate: Boolean,
    val isNicknameNotDuplicated: Boolean,
    val isDepartmentValidate: Boolean,
)
