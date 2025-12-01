package com.teamhy2.feature.setting.presentation

import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.user.domain.model.UserInfo
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
) : MviViewModel<SettingUiIntent, SettingUiState, SettingSideEffect>(SettingUiState.Loading) {
    override suspend fun reduceState(
        current: SettingUiState,
        intent: SettingUiIntent,
    ): SettingUiState {
        return when (intent) {
            SettingUiIntent.EnterSettingScreen -> initSettingUiState(current = current)
            SettingUiIntent.RequestSignOut -> signOut(current = current)
            SettingUiIntent.RequestWithdraw -> withdraw(current = current)
            is SettingUiIntent.ToggleNotificationPermission ->
                updateNotificationSwitchState(
                    current = current,
                    isChecked = intent.isChecked,
                )
        }
    }

    private suspend fun initSettingUiState(current: SettingUiState): SettingUiState =
        coroutineScope {
            val userInfo: Deferred<Result<UserInfo>> =
                async { userRepository.getUserInfo() }

            val notificationSwitchState: Deferred<Boolean> =
                async { settingsRepository.notificationSwitchState.first() }

            runCatching {
                SettingUiState.Success(
                    isNotificationSwitchChecked = notificationSwitchState.await(),
                    userInfo = userInfo.await().getOrThrow(),
                )
            }.fold(
                onSuccess = { successState ->
                    successState
                },
                onFailure = { throwable ->
                    postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    current
                },
            )
        }

    private suspend fun signOut(current: SettingUiState): SettingUiState {
        return userRepository.signOut()
            .fold(
                onSuccess = {
                    SettingUiState.Expired
                },
                onFailure = { throwable ->
                    postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    current
                },
            )
    }

    private suspend fun withdraw(current: SettingUiState): SettingUiState {
        return userRepository.withdraw()
            .fold(
                onSuccess = {
                    SettingUiState.Expired
                },
                onFailure = { throwable ->
                    postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    current
                },
            )
    }

    private suspend fun updateNotificationSwitchState(
        current: SettingUiState,
        isChecked: Boolean,
    ): SettingUiState {
        return settingsRepository.saveNotificationSwitchState(isChecked)
            .fold(
                onSuccess = {
                    runOn<SettingUiState.Success>(current) {
                        copy(isNotificationSwitchChecked = isChecked)
                    }
                },
                onFailure = { throwable ->
                    postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    current
                },
            )
    }
}
