package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SettingsEvent {
    data class NotificationSwitchChanged(val isChecked: Boolean) : SettingsEvent

    object Logout : SettingsEvent

    object Withdraw : SettingsEvent
}

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) : ViewModel() {
        private val _settingUiState = MutableStateFlow(SettingUiState())
        val settingUiState: StateFlow<SettingUiState> = _settingUiState.asStateFlow()

        init {
            loadNotificationSwitchState()
        }

        private fun loadNotificationSwitchState() {
            viewModelScope.launch {
                repository.notificationSwitchState.collectLatest { isChecked ->
                    _settingUiState.value =
                        settingUiState.value.copy(isNotificationSwitchChecked = isChecked)
                }
            }
        }

        fun onEvent(event: SettingsEvent) {
            when (event) {
                is SettingsEvent.NotificationSwitchChanged -> {
                    viewModelScope.launch {
                        repository.saveNotificationSwitchState(event.isChecked)
                        _settingUiState.value =
                            settingUiState.value.copy(isNotificationSwitchChecked = event.isChecked)
                    }
                }

                SettingsEvent.Logout -> {
                    // 로그아웃 처리 로직
                }

                SettingsEvent.Withdraw -> {
                    // 회원탈퇴 처리 로직
                }
            }
        }
    }
