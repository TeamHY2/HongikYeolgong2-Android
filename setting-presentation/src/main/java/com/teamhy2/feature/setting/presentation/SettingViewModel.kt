package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.onboarding.domain.repository.UserRepository
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
        private val repository: SettingsRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _settingUiState = MutableStateFlow(SettingUiState(isSignedOutOrWithDraw = false))
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

        fun logout() {
//            AuthUI.getInstance().signOut(context)
            // TODO: 서버 마이그레이션
        }

        fun withdraw() {
            viewModelScope.launch {
                userRepository.withdraw()
                    .onSuccess {
                        _settingUiState.update { it.copy(isSignedOutOrWithDraw = true) }
                    }
                    .onFailure {
                        // TODO: error flow 등록
                    }
            }
        }

        fun updateNotificationSwitchState(isChecked: Boolean) {
            viewModelScope.launch {
                repository.saveNotificationSwitchState(isChecked)
                _settingUiState.value =
                    settingUiState.value.copy(isNotificationSwitchChecked = isChecked)
            }
        }
    }
