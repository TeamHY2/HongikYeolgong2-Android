package com.teamhy2.record

import androidx.lifecycle.ViewModel
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.domain.repository.CalendarStudyDayRepository
import com.teamhy2.record.domain.repository.StudyDurationRepository
import com.teamhy2.record.util.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RecordViewModel
    @Inject
    constructor(
        private val studyDurationRepository: StudyDurationRepository,
        private val calendarStudyDayRepository: CalendarStudyDayRepository,
    ) : ViewModel(), ContainerHost<RecordState, RecordSideEffect> {
        override val container: Container<RecordState, RecordSideEffect> =
            container(RecordState(isLoading = true, studyDuration = StudyDuration.DEFAULT))

        fun fetchStudyDuration() =
            intent {
                reduce { state.copy(isLoading = true) }

                studyDurationRepository.fetchStudyDuration()
                    .onSuccess { studyDuration ->
                        reduce { state.copy(isLoading = false, studyDuration = studyDuration) }
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(RecordSideEffect.ShowError(it))
                    }
            }

        fun fetchCalendar() =
            intent {
                reduce { state.copy(isLoading = true) }
                calendarStudyDayRepository.updateCalendarStudyDay()
                    .onSuccess {
                        calendarStudyDayRepository.fetchStudyDaysForYearMonth(LocalDate.now())
                            .onSuccess { calendarStudyDays ->
                                reduce {
                                    state.copy(isLoading = false, calendar = Calendar(studyDays = calendarStudyDays))
                                }
                            }
                            .onFailure {
                                reduce { state.copy(isLoading = false) }
                                postSideEffect(RecordSideEffect.ShowError(it))
                            }
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(RecordSideEffect.ShowError(it))
                    }
            }

        fun updateCalendarMonth(isNextMonth: Boolean) =
            intent {
                reduce { state.copy(isLoading = true, selectedStudyDay = null) }
                val updatedCalendar =
                    state.calendar.apply {
                        if (isNextMonth) moveToNextMonth() else moveToPreviousMonth()
                    }

                calendarStudyDayRepository.fetchStudyDaysForYearMonth(updatedCalendar.date)
                    .onSuccess { studyDays ->
                        reduce {
                            state.copy(
                                isLoading = false,
                                calendar = updatedCalendar.copy(studyDays = studyDays),
                            )
                        }
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(RecordSideEffect.ShowError(it))
                    }
            }

        fun updateSelectedStudyDay(studyDay: StudyDay?) =
            intent {
                if (studyDay == null) {
                    reduce { state.copy(selectedStudyDay = null) }
                    return@intent
                }

                studyDurationRepository.fetchStudyDuration(studyDay.date)
                    .onSuccess { studyDuration ->
                        reduce {
                            state.copy(
                                selectedStudyDay =
                                    SelectedStudyDay(
                                        studyDay = studyDay,
                                        formattedDate = studyDay.date.toFormattedString(),
                                        studyDuration = studyDuration,
                                    ),
                            )
                        }
                    }
                    .onFailure {
                        postSideEffect(RecordSideEffect.ShowError(it))
                    }
            }
    }
