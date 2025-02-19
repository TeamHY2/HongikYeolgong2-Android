package com.teamhy2.record

import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.record.domain.model.StudyDuration

data class RecordState(
    val isLoading: Boolean = false,
    val studyDuration: StudyDuration,
    val calendar: Calendar = Calendar(),
)

sealed interface RecordSideEffect {
    data class ShowError(val throwable: Throwable) : RecordSideEffect
}
