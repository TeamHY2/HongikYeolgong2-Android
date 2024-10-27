package com.teamhy2.record

import androidx.lifecycle.ViewModel
import com.teamhy2.record.model.RecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecordViewModel
    @Inject
    constructor() : ViewModel() {
        private val _recordUiState = MutableStateFlow<RecordUiState>(RecordUiState.Loading)
        val recordUiState: StateFlow<RecordUiState> = _recordUiState.asStateFlow()

        init {
            loadRecordData()
        }

        private fun loadRecordData() {
            // TODO: Calendar와 StudySummary 서버로부터 불러 오는 로직을 구현
        }

        fun updateCalendarMonth(isNextMonth: Boolean) {
            // TODO: 달력 이동 구현
        }
    }
