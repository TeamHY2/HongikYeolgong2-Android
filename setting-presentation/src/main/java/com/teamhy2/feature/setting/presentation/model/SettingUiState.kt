package com.teamhy2.feature.setting.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teamhy2.user.domain.model.UserInfo

@Stable
sealed interface SettingUiState {
    @Immutable
    data object Loading : SettingUiState

    @Immutable
    data class Success(
        val isNotificationSwitchChecked: Boolean,
        val userInfo: UserInfo,
    ) : SettingUiState

    @Immutable
    data object Expired : SettingUiState

    @Immutable
    data class Error(val message: String) : SettingUiState
}
