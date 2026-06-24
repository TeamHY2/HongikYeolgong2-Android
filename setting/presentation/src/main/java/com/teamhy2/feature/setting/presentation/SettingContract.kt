package com.teamhy2.feature.setting.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.user.domain.model.UserInfo

@Stable
sealed interface SettingUiState : UiState {
    @Immutable
    data object Loading : SettingUiState

    @Immutable
    data class Success(
        val isNotificationSwitchChecked: Boolean,
        val userInfo: UserInfo,
    ) : SettingUiState

    @Immutable
    data object Expired : SettingUiState
}

sealed interface SettingUiIntent : UiIntent {
    data object EnterSettingScreen : SettingUiIntent

    data object RequestWithdraw : SettingUiIntent

    data object RequestSignOut : SettingUiIntent

    data class ToggleNotificationPermission(val isChecked: Boolean) : SettingUiIntent
}

sealed interface SettingSideEffect : SideEffect {
    data class ShowSnackBar(val throwable: Throwable) : SettingSideEffect
}
