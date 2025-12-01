package com.teamhy2.feature.setting.presentation

import androidx.lifecycle.ViewModel
import com.teamhy2.onboarding.NicknameState
import com.teamhy2.onboarding.domain.model.NicknameValidation
import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileModificationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository,
) : ViewModel(), ContainerHost<ProfileModificationState, ProfileModificationSideEffect> {
    override val container: Container<ProfileModificationState, ProfileModificationSideEffect> =
        container(ProfileModificationState())

    init {
        initSettingUiState()
    }

    private fun initSettingUiState() =
        intent {
            reduce { state.copy(isLoading = true) }
            val departments = departmentRepository.getAllDepartments()

            userRepository.getUserInfo()
                .onSuccess { userInfo ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            userInfo = userInfo,
                            departments = departments,
                            nicknameState = NicknameState.DUPLICATED,
                            isNicknameValidate = true,
                            isDepartmentValidate = true,
                        )
                    }
                }
                .onFailure { throwable ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ProfileModificationSideEffect.ShowError(throwable))
                }
        }

    fun updateNickname(value: String) =
        intent {
            reduce {
                state.copy(
                    userInfo = state.userInfo.copy(nickname = value),
                    nicknameState = NicknameState.NOT_CHECKED,
                )
            }
            checkNicknameValidation()
        }

    private fun checkNicknameValidation() =
        intent {
            reduce {
                state.copy(isNicknameValidate = NicknameValidation.validate(state.userInfo.nickname))
            }
        }

    fun updateDepartment(value: String) =
        intent {
            reduce {
                state.copy(
                    userInfo = state.userInfo.copy(department = value),
                )
            }
            checkDepartmentValidation()
        }

    private fun checkDepartmentValidation() =
        intent {
            reduce {
                state.copy(
                    isDepartmentValidate = state.departments.find { it == state.userInfo.department } != null,
                )
            }
        }

    fun checkNicknameDuplication() =
        intent {
            userRepository.checkNicknameDuplication(state.userInfo.nickname)
                .onSuccess { isDuplicated ->
                    if (isDuplicated) {
                        reduce { state.copy(nicknameState = NicknameState.DUPLICATED) }
                        return@onSuccess
                    }
                    reduce { state.copy(nicknameState = NicknameState.NOT_DUPLICATED) }
                }
                .onFailure { throwable ->
                    postSideEffect(ProfileModificationSideEffect.ShowError(throwable))
                }
        }

    fun updateUserInfo() =
        intent {
            reduce { state.copy(isLoading = true) }
            userRepository.modifyUserInfo(state.userInfo.nickname, state.userInfo.department)
                .onSuccess {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ProfileModificationSideEffect.ProfileModificationSucceed)
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ProfileModificationSideEffect.ProfileModificationFailure)
                }
        }
}
