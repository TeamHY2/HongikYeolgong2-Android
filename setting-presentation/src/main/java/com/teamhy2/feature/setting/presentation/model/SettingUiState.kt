package com.teamhy2.feature.setting.presentation.model

import com.teamhy2.feature.setting.domain.repository.model.UserInfo

sealed interface SettingUiState {
    object Loading : SettingUiState

    data class Success(
        val isNotificationSwitchChecked: Boolean,
        val userInfo: UserInfo,
    ) : SettingUiState

    data class Error(val message: String) : SettingUiState
}
