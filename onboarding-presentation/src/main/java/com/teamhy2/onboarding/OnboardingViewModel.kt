package com.teamhy2.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benenfeldt.remote.token.JwtManager
import com.teamhy2.core.auth.GoogleSignIn
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
class OnboardingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val googleSignIn: GoogleSignIn,
        private val jwtManager: JwtManager,
    ) : ViewModel() {
        private val _signInState: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.Idle)
        val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        fun signInWithGoogleIdToken() {
            viewModelScope.launch {
                googleSignIn.requestSignInWithIdToken()
                    .onSuccess { idToken: String ->
                        requestSignInToServerWithIdToken(idToken)
                        jwtManager.saveGoogleIdToken(idToken)
                    }
                    .onFailure { throwable ->
                        _signInState.update { SignInState.Failure }
                        _errorFlow.emit(throwable)
                    }
            }
        }

        private suspend fun requestSignInToServerWithIdToken(idToken: String) {
            userRepository.signIn(idToken)
                .onSuccess { isAlreadyExist ->
                    if (isAlreadyExist) {
                        _signInState.update { SignInState.SuccessfulSignedInUser }
                        return@onSuccess
                    }
                    _signInState.update { SignInState.SuccessfulSignedInGuest }
                }
                .onFailure { throwable ->
                    _signInState.update { SignInState.Failure }
                    _errorFlow.emit(throwable)
                }
        }
    }
