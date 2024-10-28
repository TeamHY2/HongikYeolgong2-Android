package com.teamhy2.record.model

import com.hongikyeolgong2.calendar.model.Calendar

sealed interface RecordUiState {
    data object Loading : RecordUiState

    data class Success(
        val studySummary: StudySummary,
        val calendar: Calendar = Calendar(studyDays = emptyList()),
    ) : RecordUiState

    data class Error(val message: String) : RecordUiState
}
