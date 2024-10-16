package com.teamhy2.feature.setting.presentation.model

import com.teamhy2.feature.setting.domain.repository.model.UserInfo

data class SettingUiState(
    val isNotificationSwitchChecked: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
)
