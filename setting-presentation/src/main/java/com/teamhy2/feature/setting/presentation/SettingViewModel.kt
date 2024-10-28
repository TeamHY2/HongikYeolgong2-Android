package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _settingUiState = MutableStateFlow<SettingUiState>(SettingUiState.Loading)
        val settingUiState: StateFlow<SettingUiState> = _settingUiState.asStateFlow()

        init {
            initSettingUiState()
        }

        private fun initSettingUiState() {
            viewModelScope.launch {
                val userInfoResult = userRepository.getUserInfo()
                settingsRepository.notificationSwitchState.collectLatest { isNotificationSwitchChecked ->
                    userInfoResult
                        .onSuccess { userInfo ->
                            _settingUiState.update {
                                SettingUiState.Success(
                                    isNotificationSwitchChecked = isNotificationSwitchChecked,
                                    userInfo = userInfo,
                                )
                            }
                        }
                        .onFailure {
                            // TODO: error flow 등록
                        }
                }
            }
        }

        fun signOut() {
            viewModelScope.launch {
                userRepository.signOut()
                    .onSuccess {
                        _settingUiState.update { SettingUiState.Expired }
                    }
                    .onFailure {
                        // TODO: error flow 등록
                    }
            }
        }

        fun withdraw() {
            viewModelScope.launch {
                userRepository.withdraw()
                    .onSuccess {
                        _settingUiState.update { SettingUiState.Expired }
                    }
                    .onFailure {
                        // TODO: error flow 등록
                    }
            }
        }

        fun updateNotificationSwitchState(isChecked: Boolean) {
            viewModelScope.launch {
                settingsRepository.saveNotificationSwitchState(isChecked)
                _settingUiState.update { currentState ->
                    if (currentState is SettingUiState.Success) {
                        currentState.copy(
                            isNotificationSwitchChecked = isChecked,
                        )
                    } else {
                        currentState
                    }
                }
            }
        }
    }
