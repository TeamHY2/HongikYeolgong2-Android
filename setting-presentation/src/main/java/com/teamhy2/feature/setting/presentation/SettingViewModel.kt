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
            loadSettings()
        }

        private fun loadSettings() {
            viewModelScope.launch {
                runCatching {
                    // TODO: 서버에서 유저 정보를 가져오는 로직으로 대체
                    UserInfo("서재원", "전자전기공학부")
                }.onSuccess { userInfo ->
                    settingsRepository.notificationSwitchState.collectLatest { isChecked ->
                        _settingUiState.update {
                            SettingUiState.Success(
                                isNotificationSwitchChecked = isChecked,
                                userInfo = userInfo,
                            )
                        }
                    }
                }.onFailure {}
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
