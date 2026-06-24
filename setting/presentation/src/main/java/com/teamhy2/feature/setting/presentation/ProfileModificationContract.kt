package com.teamhy2.feature.setting.presentation

import com.teamhy2.onboarding.NicknameState
import com.teamhy2.user.domain.model.UserInfo

data class ProfileModificationState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo = UserInfo.LOADING,
    val departments: List<String> = emptyList(),
    val isNicknameValidate: Boolean = false,
    val nicknameState: NicknameState = NicknameState.NOT_CHECKED,
    val isDepartmentValidate: Boolean = false,
)

sealed interface ProfileModificationSideEffect {
    data class ShowError(val throwable: Throwable) : ProfileModificationSideEffect

    data object ProfileModificationSucceed : ProfileModificationSideEffect

    data object ProfileModificationFailure : ProfileModificationSideEffect
}
