package com.teamhy2.feature.main

import androidx.lifecycle.ViewModel
import com.teamhy2.feature.main.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : ViewModel() {
        private val _mainUiState = MutableStateFlow(MainUiState())
        val mainUiState: StateFlow<MainUiState> = _mainUiState
    }
