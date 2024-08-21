package com.teamhy2.feature.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.main.domain.WebViewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val webViewRepository: WebViewRepository,
    ) : ViewModel() {
        private val _mainUiState = MutableStateFlow(MainUiState())
        val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

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
    }
