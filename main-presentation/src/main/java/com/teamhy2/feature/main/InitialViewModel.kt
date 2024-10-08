package com.teamhy2.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.feature.main.navigation.Main
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.onboarding.domain.repository.UserRepository
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.SignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class InitialViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
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

        fun fetchStartDestination() {
            val uid: String? = Firebase.auth.currentUser?.uid

            fun setStartDestination(startDestination: String) {
                if (_initialUiState.value is InitialUiState.Loading) {
                    _initialUiState.value =
                        InitialUiState.Success(startDestination = startDestination)
                    return
                }
                if(_initialUiState.value is InitialUiState.Success) {
                    _initialUiState.value = (_initialUiState.value as InitialUiState.Success).copy(startDestination = startDestination)
                    return
                }
            }

            if (uid == null) {
                // 1. UID가 null -> 구글로그인이 안되어 있음
                setStartDestination(Onboarding.ROUTE)
                return
            } else {
                viewModelScope.launch {
                    val isUserExists: Boolean = userRepository.checkUserExists(uid)

                    if (isUserExists) {
                        // 2. UID가 null이 아니고 isUserExists가 true  -> 구글로그인이 되어 있고 유저 데이터가 존재함
                        setStartDestination(Main.ROUTE)
                    } else {
                        // 3. UID가 null이 아니고 isUserExists가 false  -> 구글로그인이 되어 있고 유저 데이터가 존재하지 않음
                        setStartDestination(SignUp.ROUTE)
                    }
                }
            }
        }

        private fun fetchFirebaseUrls() {
            viewModelScope.launch {
                val urls = webViewRepository.fetchFirebaseUrls()
                if (_initialUiState.value is InitialUiState.Loading) {
                    _initialUiState.value = InitialUiState.Success(urls = urls)
                    return@launch
                }
                if (_initialUiState.value is InitialUiState.Success){
                    _initialUiState.value = (_initialUiState.value as InitialUiState.Success).copy(urls = urls)
                    return@launch
                }
            }
        }

        fun getMinVersion(currentVersion: Long) {
            viewModelScope.launch {
                val firebaseStore = FirebaseFirestore.getInstance()

                runCatching {
                    firebaseStore.collection(COLLECTION_APP_VERSION).document(DOCUMENT_ANDROID).get().await()
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

    companion object{
        private const val COLLECTION_APP_VERSION ="AppVersion"
        private const val DOCUMENT_ANDROID ="Android"

    }
    }
