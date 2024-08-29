package com.teamhy2.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.onboarding.domain.repository.UserRepository
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val webViewRepository: WebViewRepository,
    ) : ViewModel() {
        private val _userExists: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

        var urls by mutableStateOf<Map<String, String>>(emptyMap())
            private set

        init {
            getFirebaseUrls()
        }

        private fun getFirebaseUrls() {
            viewModelScope.launch {
                val urlList = webViewRepository.fetchFirebaseUrls()
                urls = urlList.toMap()
            }
        }

        fun checkUserExists(uid: String?) {
            if (uid == null) {
                _userExists.value = false
                return
            }

            viewModelScope.launch {
                _userExists.value = userRepository.checkUserExists(uid)
            }
        }
    }
