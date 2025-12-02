package com.teamhy2.onboarding

import androidx.lifecycle.ViewModel
import com.teamhy2.core.auth.GoogleSignIn
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), ContainerHost<OnboardingState, OnboardingSideEffect> {
    override val container: Container<OnboardingState, OnboardingSideEffect> =
        container(OnboardingState)

    fun signInWithGoogleIdToken(googleSignIn: GoogleSignIn) =
        intent {
            googleSignIn.requestSignInWithIdToken()
                .onSuccess { idToken: String ->
                    requestSignInToServerWithIdToken(idToken)
                }
                .onFailure { throwable ->
                    postSideEffect(
                        OnboardingSideEffect.ShowError(throwable.message),
                    )
                }
        }

    private fun requestSignInToServerWithIdToken(idToken: String) =
        intent {
            userRepository.signIn(idToken)
                .onSuccess { isAlreadyExist ->
                    when (isAlreadyExist) {
                        true -> postSideEffect(OnboardingSideEffect.NavigateToHome)
                        false -> postSideEffect(OnboardingSideEffect.NavigateToSignIn)
                    }
                }
                .onFailure { throwable ->
                    postSideEffect(
                        OnboardingSideEffect.ShowError(throwable.message),
                    )
                }
        }
}
