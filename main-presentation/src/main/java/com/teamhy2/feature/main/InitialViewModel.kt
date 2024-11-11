package com.teamhy2.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benenfeldt.remote.token.JwtManager
import com.benenfeldt.remote.token.TokenRole
import com.benenfeldt.remote.token.TokenValidator
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.feature.home.navigation.Home
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.SignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class InitialViewModel
    @Inject
    constructor(
        private val webViewRepository: WebViewRepository,
        private val jwtManager: JwtManager,
        private val tokenValidator: TokenValidator,
        val notificationHandler: NotificationHandler,
    ) : ViewModel() {
        private val _initialUiState: MutableStateFlow<InitialUiState> =
            MutableStateFlow(InitialUiState.Loading)
        val initialUiState: StateFlow<InitialUiState> = _initialUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            initInitialState()
        }

        private fun initInitialState() {
            viewModelScope.launch {
                val startDestination: Deferred<String> = async { getStartDestination() }
                val urls: Deferred<Map<String, String>> = async { webViewRepository.fetchFirebaseUrls() }

                _initialUiState.update {
                    InitialUiState.Success(
                        startDestination = startDestination.await(),
                        urls = urls.await(),
                    )
                }
            }
        }

        private suspend fun getStartDestination(): String {
            jwtManager.getAccessJwt() ?: return resetToken()

            val isValidToken = tokenValidator.validate().getOrNull()
            if (isValidToken == null) {
                _errorFlow.emit(Throwable("서버 연결에 이상이 있습니다."))
                return resetToken()
            }

            return when (isValidToken.role) {
                TokenRole.USER -> Home.ROUTE
                TokenRole.GUEST -> SignUp.ROUTE
                TokenRole.ADMIN -> resetToken()
            }
        }

        private suspend fun resetToken(): String {
            jwtManager.clearAllTokens()
            return Onboarding.ROUTE
        }

        fun getMinVersion(currentVersion: Long) {
            viewModelScope.launch {
                val firebaseStore = FirebaseFirestore.getInstance()

                runCatching {
                    firebaseStore.collection(COLLECTION_APP_VERSION).document(DOCUMENT_ANDROID).get()
                        .await()
                }
                    .onSuccess {
                        val minVersion = it.get("minVersion")
                        if (minVersion.toString().toLong() > currentVersion) {
                            _initialUiState.value = InitialUiState.NeedUpdate
                        }
                    }
                    .onFailure {
                        Log.d("FireStore", "getMinVersion: ${it.message}")
                    }
            }
        }

        companion object {
            private const val COLLECTION_APP_VERSION = "AppVersion"
            private const val DOCUMENT_ANDROID = "Android"
        }
    }
