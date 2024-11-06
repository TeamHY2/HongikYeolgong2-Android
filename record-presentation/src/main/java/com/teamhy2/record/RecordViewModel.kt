package com.teamhy2.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.domain.repository.CalendarStudyDayRepository
import com.teamhy2.record.domain.repository.StudyDurationRepository
import com.teamhy2.record.model.RecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
                val studyDurationResult: Result<StudyDuration> =
                    studyDurationRepository.fetchStudyDuration()
                val updateCalendarResult: Result<Unit> =
                    calendarStudyDayRepository.updateCalendarStudyDay()

                if (updateCalendarResult.isSuccess) {
                    calendarStudyDayRepository.fetchStudyDaysForYearMonth(LocalDate.now())
                        .onSuccess { calendarStudyDays ->
                            val studyDuration: StudyDuration =
                                studyDurationResult.getOrNull() ?: StudyDuration.DEFAULT

                            _recordUiState.update {
                                RecordUiState.Success(
                                    studyDuration = studyDuration,
                                    calendar = Calendar(studyDays = calendarStudyDays),
                                )
                            }
                        }.onFailure { exception ->
                            _errorFlow.emit(exception)
                        }
                } else {
                    updateCalendarResult.exceptionOrNull()?.let { _errorFlow.emit(it) }
                }

                if (studyDurationResult.isFailure) {
                    studyDurationResult.exceptionOrNull()?.let { _errorFlow.emit(it) }
                }
            }
        }

        fun updateCalendarMonth(isNextMonth: Boolean) {
            val currentState: RecordUiState = _recordUiState.value
            if (currentState is RecordUiState.Success) {
                val updatedCalendar: Calendar =
                    currentState.calendar.apply {
                        if (isNextMonth) {
                            moveToNextMonth()
                        } else {
                            moveToPreviousMonth()
                        }
                    }

                viewModelScope.launch {
                    calendarStudyDayRepository.fetchStudyDaysForYearMonth(updatedCalendar.date)
                        .onSuccess { studyDays ->
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
