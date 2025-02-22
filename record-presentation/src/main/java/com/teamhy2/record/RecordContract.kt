package com.teamhy2.record

import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.util.toDateState
import java.time.LocalDate

data class RecordState(
    val isLoading: Boolean = false,
    val date: String = LocalDate.now().toDateState(),
    val studyDuration: StudyDuration,
    val selectedStudyDay: SelectedStudyDay? = null,
    val calendar: Calendar = Calendar(),
)

data class SelectedStudyDay(
    val studyDay: StudyDay,
    val date: String = studyDay.date.toDateState(),
    val studyDuration: StudyDuration,
)

sealed interface RecordSideEffect {
    data class ShowError(val throwable: Throwable) : RecordSideEffect
}
