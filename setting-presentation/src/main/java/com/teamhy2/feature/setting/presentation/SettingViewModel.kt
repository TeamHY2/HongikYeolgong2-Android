package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val userRepository: UserRepository,
    ) : ViewModel(), ContainerHost<SettingState, SettingSideEffect> {
        override val container: Container<SettingState, SettingSideEffect> =
            container(SettingState())

        fun initSettingScreen() =
            intent {
                reduce {
                    SettingState(
                        isLoading = true,
                    )
                }

                userRepository.getUserInfo()
                    .onSuccess { userInfo ->
                        // Notification 구독
                        settingsRepository.notificationSwitchState
                            .collectLatest { isNotificationSwitchChecked ->
                                reduce {
                                    state.copy(
                                        isLoading = false,
                                        userInfo = userInfo,
                                        isNotificationSwitchChecked = isNotificationSwitchChecked,
                                    )
                                }
                            }
                    }
                    .onFailure { throwable ->
                        reduce {
                            state.copy(
                                isLoading = false,
                            )
                        }

                        postSideEffect(
                            SettingSideEffect.ShowError(
                                throwable.message ?: "알 수 없는 문제가 생겼어요",
                            ),
                        )
                    }
            }

        fun logout() =
            intent {
                userRepository.signOut()
                    .onSuccess {
                        postSideEffect(SettingSideEffect.LogoutOrWithdrawComplete)
                    }
                    .onFailure { throwable ->
                        postSideEffect(
                            SettingSideEffect.ShowError(
                                throwable.message ?: "알수 없는 문제가 생겼어요",
                            ),
                        )
                    }
            }

        fun withdraw() =
            intent {
                userRepository.withdraw()
                    .onSuccess {
                        postSideEffect(SettingSideEffect.LogoutOrWithdrawComplete)
                    }
                    .onFailure { throwable ->
                        SettingSideEffect.ShowError(
                            throwable.message ?: "알수 없는 문제가 생겼어요",
                        )
                    }
            }

        fun toggleNotificationSwitch(isChecked: Boolean) =
            intent {
                settingsRepository.saveNotificationSwitchState(isChecked)
            }

        fun onInquiryButtonClick() =
            intent {
                postSideEffect(SettingSideEffect.Navigation.Inquiry)
            }

        fun onNoticeButtonClick() =
            intent {
                postSideEffect(SettingSideEffect.Navigation.Notice)
            }

        fun onProfileModifyClick() =
            intent {
                postSideEffect(SettingSideEffect.Navigation.ProfileModification)
            }
    }
