package com.teamhy2.feature.setting.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.domain.repository.model.UserInfo
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.onboarding.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
            loadSettings()
        }

        private fun loadSettings() {
            viewModelScope.launch {
                try {
                    // TODO: 비동기로 UserInfo를 가져오는 코드를 추가
                    val userInfo = UserInfo("서재원", "전자전기공학부")

                    settingsRepository.notificationSwitchState.collectLatest { isChecked ->
                        _settingUiState.value =
                            SettingUiState.Success(
                                isNotificationSwitchChecked = isChecked,
                                userInfo = userInfo,
                            )
                    }
                } catch (e: Exception) {
                    _settingUiState.value =
                        SettingUiState.Error("Failed to load settings: ${e.message}")
                }
            }
        }

        fun onLogoutClick(context: Context) {
            AuthUI.getInstance().signOut(context)
        }

        fun onWithdrawClick(context: Context) {
            viewModelScope.launch {
                userRepository.withdraw()
                AuthUI.getInstance().signOut(context)
            }
        }

        fun updateNotificationSwitchState(isChecked: Boolean) {
            viewModelScope.launch {
                settingsRepository.saveNotificationSwitchState(isChecked)
                val currentState = _settingUiState.value
                if (currentState is SettingUiState.Success) {
                    _settingUiState.value =
                        currentState.copy(
                            isNotificationSwitchChecked = isChecked,
                        )
                }
            }
        }
    }
