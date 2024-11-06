package com.teamhy2.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.domain.repository.CalendarStudyDayRepository
import com.teamhy2.record.domain.repository.StudyDurationRepository
import com.teamhy2.record.model.RecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RecordViewModel
    @Inject
    constructor(
        private val studyDurationRepository: StudyDurationRepository,
        private val calendarStudyDayRepository: CalendarStudyDayRepository,
    ) : ViewModel() {
        private val _recordUiState = MutableStateFlow<RecordUiState>(RecordUiState.Loading)
        val recordUiState: StateFlow<RecordUiState> = _recordUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            loadRecordData()
        }

        private fun loadRecordData() {
            viewModelScope.launch {
                runCatching {
                    val results =
                        listOf(
                            async { studyDurationRepository.fetchStudyDuration().getOrThrow() },
                            async { calendarStudyDayRepository.updateCalendarStudyDay().getOrThrow() },
                        ).awaitAll()

                    val now: LocalDate = LocalDate.now()
                    val calendarStudyDays: List<StudyDay> =
                        async { calendarStudyDayRepository.fetchStudyDaysForYearMonth(now) }.await()

                    _recordUiState.update {
                        RecordUiState.Success(
                            studyDuration = results[0] as StudyDuration,
                            calendar = Calendar(studyDays = calendarStudyDays),
                        )
                    }
                }.onFailure { exception ->
                    _errorFlow.emit(exception)
                }
            }
        }

        fun updateCalendarMonth(isNextMonth: Boolean) {
            val currentState = _recordUiState.value
            if (currentState is RecordUiState.Success) {
                val updatedCalendar =
                    currentState.calendar.apply {
                        if (isNextMonth) {
                            moveToNextMonth()
                        } else {
                            moveToPreviousMonth()
                        }
                    }

                viewModelScope.launch {
                    runCatching {
                        calendarStudyDayRepository.fetchStudyDaysForYearMonth(updatedCalendar.date)
                    }.onSuccess { studyDays ->
                        _recordUiState.update {
                            RecordUiState.Success(
                                studyDuration = currentState.studyDuration,
                                calendar = updatedCalendar.copy(studyDays = studyDays),
                            )
                        }
                    }.onFailure { exception ->
                        _errorFlow.emit(exception)
                    }
                }
            }
        }
    }
