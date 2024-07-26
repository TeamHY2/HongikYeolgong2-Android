package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.setting.data.datasource.SettingsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val isNotificationSwitchChecked: Boolean = false,
)

sealed interface SettingsEvent {
    data class NotificationSwitchChanged(val isChecked: Boolean) : SettingsEvent

    object Logout : SettingsEvent

    object Withdraw : SettingsEvent
}

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val dataSource: SettingsDataSource,
    ) : ViewModel() {
        private val _state = MutableStateFlow(SettingsState())
        val state: StateFlow<SettingsState> = _state

        init {
            loadNotificationSwitchState()
        }

        private fun loadNotificationSwitchState() {
            viewModelScope.launch {
                val isChecked = dataSource.getNotificationSwitchState()
                _state.value = state.value.copy(isNotificationSwitchChecked = isChecked)
            }
        }

        fun onEvent(event: SettingsEvent) {
            when (event) {
                is SettingsEvent.NotificationSwitchChanged -> {
                    viewModelScope.launch {
                        dataSource.saveNotificationSwitchState(event.isChecked)
                        _state.value = state.value.copy(isNotificationSwitchChecked = event.isChecked)
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
