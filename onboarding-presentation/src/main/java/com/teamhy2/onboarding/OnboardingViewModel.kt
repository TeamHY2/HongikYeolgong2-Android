package com.teamhy2.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.core.auth.SocialSignIn
import com.teamhy2.onboarding.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val socialSignIn: SocialSignIn,
    ) : ViewModel() {
        private val _signInState: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.Idle)
        val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

        fun signInWithGoogleIdToken() {
            viewModelScope.launch {
                socialSignIn.requestSignInWithIdToken()
                    .onSuccess { idToken: String ->
                        requestSignInToServerWithIdToken(idToken)
                    }
                    .onFailure {
                        _signInState.update { SignInState.Failure }
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
                .onFailure {
                    _signInState.update { SignInState.Failure }
                }
        }
    }
