package com.teamhy2.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
    ) : ViewModel() {
        private val _homeUiState = MutableStateFlow(HomeUiState())
        val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            getWiseSaying()
        }

        private fun getWiseSaying() {
            viewModelScope.launch {
                wiseSayingRepository.fetchWiseSaying()
                    .onSuccess { wiseSaying ->
                        _homeUiState.value = _homeUiState.value.copy(wiseSaying = wiseSaying)
                    }
                    .onFailure { exception ->
                        _errorFlow.emit(exception)
                    }
            }
        }
    }
