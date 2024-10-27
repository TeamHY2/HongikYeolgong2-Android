package com.teamhy2.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import com.teamhy2.onboarding.navigation.Onboarding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        val notificationHandler: NotificationHandler,
    ) : ViewModel() {
        private val _initialUiState: MutableStateFlow<InitialUiState> =
            MutableStateFlow(InitialUiState.Loading)
        val initialUiState: StateFlow<InitialUiState> = _initialUiState.asStateFlow()

        init {
            fetchStartDestination()
            fetchFirebaseUrls()
        }

        private fun fetchStartDestination() {
            fun setStartDestination(startDestination: String) {
                if (_initialUiState.value is InitialUiState.Loading) {
                    _initialUiState.update { InitialUiState.Success(startDestination = startDestination) }
                    return
                }
                if (_initialUiState.value is InitialUiState.Success) {
                    _initialUiState.update {
                        (it as InitialUiState.Success).copy(startDestination = startDestination)
                    }
                }
            }

            // TODO: 자동 로그인 구현 시 변경
            setStartDestination(Onboarding.ROUTE)
        }

        private fun fetchFirebaseUrls() {
            viewModelScope.launch {
                val urls = webViewRepository.fetchFirebaseUrls()
                if (_initialUiState.value is InitialUiState.Loading) {
                    _initialUiState.update { InitialUiState.Success(urls = urls) }
                    return@launch
                }
                if (_initialUiState.value is InitialUiState.Success) {
                    _initialUiState.update { (it as InitialUiState.Success).copy(urls = urls) }
                }
            }
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
