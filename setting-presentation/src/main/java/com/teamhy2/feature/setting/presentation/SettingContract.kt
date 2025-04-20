package com.teamhy2.feature.setting.presentation

import androidx.compose.runtime.Immutable
import com.teamhy2.user.domain.model.UserInfo

@Immutable
data class SettingState(
    val userInfo: UserInfo = UserInfo.LOADING,
    val isLoading: Boolean = false,
    val isNotificationSwitchChecked: Boolean = false,
)

sealed interface SettingSideEffect {
    data class ShowError(val errorMessage: String) : SettingSideEffect

    data object LogoutOrWithdrawComplete : SettingSideEffect

    sealed interface Navigation : SettingSideEffect {
        data object Inquiry : Navigation

        data object ProfileModification : Navigation

        data object Notice : Navigation
    }
}

class OnClick {
    var logout: () -> Unit = {}
    var withdraw: () -> Unit = {}
    var notificationSwitch: (Boolean) -> Unit = {}
    var notice: () -> Unit = {}
    var inquiry: () -> Unit = {}
    var profileModify: () -> Unit = {}
}
