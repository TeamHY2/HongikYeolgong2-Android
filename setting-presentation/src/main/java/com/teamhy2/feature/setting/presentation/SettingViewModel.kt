package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.viewModelScope
import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val userRepository: UserRepository,
    ) : MviViewModel<SettingUiIntent, SettingUiState, SettingSideEffect>(SettingUiState.Loading) {
        override fun handleIntent(intent: SettingUiIntent) {
            when (intent) {
                is SettingUiIntent.SignOut -> signOut()
                is SettingUiIntent.Withdraw -> withdraw()
                is SettingUiIntent.UpdateNotificationSwitchState -> updateNotificationSwitchState(intent.isChecked)
            }
        }

        fun initSettingUiState() {
            viewModelScope.launch {
                val userInfoResult = userRepository.getUserInfo()
                settingsRepository.notificationSwitchState.collectLatest { isNotificationSwitchChecked ->
                    userInfoResult
                        .onSuccess { userInfo ->
                            reduce {
                                SettingUiState.Success(
                                    isNotificationSwitchChecked = isNotificationSwitchChecked,
                                    userInfo = userInfo,
                                )
                            }
                        }
                        .onFailure { throwable ->
                            postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                        }
                }
            }
        }

        private fun signOut() {
            viewModelScope.launch {
                userRepository.signOut()
                    .onSuccess {
                        reduce { SettingUiState.Expired }
                    }
                    .onFailure { throwable ->
                        postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    }
            }
        }

        private fun withdraw() {
            viewModelScope.launch {
                userRepository.withdraw()
                    .onSuccess {
                        reduce { SettingUiState.Expired }
                    }
                    .onFailure { throwable ->
                        postSideEffect(SettingSideEffect.ShowSnackBar(throwable))
                    }
            }
        }

        private fun updateNotificationSwitchState(isChecked: Boolean) {
            viewModelScope.launch {
                settingsRepository.saveNotificationSwitchState(isChecked)
            }
        }
    }
