package com.teamhy2.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.onboarding.domain.model.NicknameValidation
import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import com.teamhy2.onboarding.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val departmentRepository: DepartmentRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _signUpUiState: MutableStateFlow<SignUpUiState> =
            MutableStateFlow(
                SignUpUiState(
                    departments = emptyList(),
                    isNicknameValidate = false,
                    nicknameState = NicknameState.NOT_CHECKED,
                    isDepartmentValidate = false,
                ),
            )
        val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

        private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
        val nickname: StateFlow<String> = _nickname.asStateFlow()

        private val _department: MutableStateFlow<String> = MutableStateFlow("")
        val department: StateFlow<String> = _department.asStateFlow()

        init {
            viewModelScope.launch {
                getAllDepartments()
            }
        }

        fun updateNickname(value: String) {
            _nickname.value = value
            _signUpUiState.value = _signUpUiState.value.copy(nicknameState = NicknameState.NOT_CHECKED)
            checkNicknameValidation()
        }

        fun updateDepartment(value: String) {
            _department.value = value
            checkDepartmentValidation()
        }

        private fun getAllDepartments() {
            viewModelScope.launch {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        departments = departmentRepository.getAllDepartments(),
                    )
            }
        }

        private fun checkNicknameValidation() {
            _signUpUiState.value =
                _signUpUiState.value.copy(isNicknameValidate = NicknameValidation.validate(_nickname.value))
        }

        private fun checkDepartmentValidation() {
            _signUpUiState.value =
                _signUpUiState.value.copy(
                    isDepartmentValidate =
                        _signUpUiState.value.departments.find { it == department.value } != null,
                )
        }

        fun checkNicknameDuplication() {
            viewModelScope.launch {
                userRepository.checkNicknameDuplication(nickname.value)
                    .onSuccess { isDuplicated ->
                        if (isDuplicated) {
                            _signUpUiState.value = _signUpUiState.value.copy(nicknameState = NicknameState.NOT_DUPLICATED)
                            return@onSuccess
                        }
                        _signUpUiState.value = _signUpUiState.value.copy(nicknameState = NicknameState.DUPLICATED)
                    }
                    .onFailure {
                        // TODO: error flow
                    }
            }
        }

        fun signUp() {
            viewModelScope.launch {
                userRepository.signUp(nickname.value, department.value)
            }
        }
    }
