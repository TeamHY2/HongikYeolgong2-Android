package com.teamhy2.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.onboarding.domain.model.NicknameValidation
import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            MutableStateFlow(SignUpUiState.Loading)
        val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

        private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
        val nickname: StateFlow<String> = _nickname.asStateFlow()

        private val _department: MutableStateFlow<String> = MutableStateFlow("")
        val department: StateFlow<String> = _department.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            viewModelScope.launch {
                getAllDepartments()
            }
        }

        fun updateNickname(value: String) {
            _nickname.value = value
            if (_signUpUiState.value is SignUpUiState.Success) {
                _signUpUiState.update { currentState ->
                    (currentState as SignUpUiState.Success).copy(
                        nicknameState = NicknameState.NOT_CHECKED,
                    )
                }
            }
            checkNicknameValidation()
        }

        fun updateDepartment(value: String) {
            _department.value = value
            checkDepartmentValidation()
        }

        private fun getAllDepartments() {
            viewModelScope.launch {
                _signUpUiState.value =
                    SignUpUiState.Success(
                        departments = departmentRepository.getAllDepartments(),
                        isNicknameValidate = false,
                        nicknameState = NicknameState.NOT_CHECKED,
                        isDepartmentValidate = false,
                    )
            }
        }

        private fun checkNicknameValidation() {
            if (_signUpUiState.value is SignUpUiState.Success) {
                _signUpUiState.update { currentState ->
                    (currentState as SignUpUiState.Success).copy(
                        isNicknameValidate = NicknameValidation.validate(_nickname.value),
                    )
                }
            }
        }

        private fun checkDepartmentValidation() {
            if (_signUpUiState.value is SignUpUiState.Success) {
                _signUpUiState.update { currentState ->
                    val success = (currentState as SignUpUiState.Success)
                    success.copy(
                        isDepartmentValidate = success.departments.find { it == department.value } != null,
                    )
                }
            }
        }

        fun checkNicknameDuplication() {
            viewModelScope.launch {
                userRepository.checkNicknameDuplication(nickname.value)
                    .onSuccess { isDuplicated ->
                        if (_signUpUiState.value is SignUpUiState.Success) {
                            val success = (_signUpUiState.value as SignUpUiState.Success)
                            if (isDuplicated) {
                                _signUpUiState.value = success.copy(nicknameState = NicknameState.DUPLICATED)
                                return@onSuccess
                            }
                            _signUpUiState.value = success.copy(nicknameState = NicknameState.NOT_DUPLICATED)
                        }
                    }
                    .onFailure { throwable ->
                        _errorFlow.emit(throwable)
                    }
            }
        }

        fun signUp() {
            viewModelScope.launch {
                userRepository.signUp(nickname.value, department.value)
                    .onSuccess {
                        _signUpUiState.value = SignUpUiState.SignUpDone
                    }
                    .onFailure { throwable ->
                        _errorFlow.emit(throwable)
                    }
            }
        }
    }
