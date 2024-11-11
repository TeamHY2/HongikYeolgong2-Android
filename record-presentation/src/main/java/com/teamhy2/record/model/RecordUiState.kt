package com.teamhy2.record.model

import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.record.domain.model.StudyDuration

sealed interface RecordUiState {
    data object Loading : RecordUiState

    data class Success(
        val studyDuration: StudyDuration,
        val calendar: Calendar = Calendar(studyDays = emptyList()),
    ) : RecordUiState

    data class Error(val message: String) : RecordUiState
}
