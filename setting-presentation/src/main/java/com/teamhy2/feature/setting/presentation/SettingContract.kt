package com.teamhy2.feature.setting.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teamhy2.designsystem.util.mvi.MviContract
import com.teamhy2.user.domain.model.UserInfo

@Stable
sealed interface SettingUiState : MviContract.UiState {
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

sealed interface SettingUiIntent : MviContract.UiIntent {
    data object Withdraw : SettingUiIntent

    data object SignOut : SettingUiIntent

    data class UpdateNotificationSwitchState(val isChecked: Boolean) : SettingUiIntent
}

sealed interface SettingSideEffect : MviContract.SideEffect {
    data class ShowSnackBar(val throwable: Throwable) : SettingSideEffect
}
