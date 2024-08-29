package com.teamhy2.feature.setting.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.onboarding.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SettingsEvent {
    data class NotificationSwitchChanged(val isChecked: Boolean) : SettingsEvent
}

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val repository: SettingsRepository,
        private val userRepository: UserRepository,
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
                    updateNotificationSwitchState(event.isChecked)
                }
            }
        }

        fun onLogoutClick(context: Context) {
            AuthUI.getInstance().signOut(context)
        }

        fun onWithdrawClick(context: Context) {
            viewModelScope.launch {
                userRepository.withdraw()
            }
            AuthUI.getInstance().signOut(context)
        }

        private fun updateNotificationSwitchState(isChecked: Boolean) {
            viewModelScope.launch {
                repository.saveNotificationSwitchState(isChecked)
                _settingUiState.value =
                    settingUiState.value.copy(isNotificationSwitchChecked = isChecked)
            }
        }
    }
