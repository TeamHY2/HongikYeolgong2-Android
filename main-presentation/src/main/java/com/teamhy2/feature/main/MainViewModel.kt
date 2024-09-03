package com.teamhy2.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.feature.main.mapper.StudyDayMapper
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import com.teamhy2.main.domain.repository.StudyDayRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val studyDayRepository: StudyDayRepository,
    ) : ViewModel() {
        private val _mainUiState = MutableStateFlow(MainUiState())
        val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

        private val studyDays = mutableMapOf<String, List<StudyDay>>()

        init {
            getWiseSaying()
            getCalendarData()
        }

        private fun getWiseSaying() {
            viewModelScope.launch {
                _mainUiState.value =
                    _mainUiState.value.copy(
                        wiseSaying = wiseSayingRepository.fetchWiseSaying(),
                    )
            }
        }

        private fun getCalendarData() {
            val uid = Firebase.auth.currentUser?.uid ?: return
            viewModelScope.launch {
                val rawStudyDaysByMonth = studyDayRepository.getStudyDays(uid)
                studyDays.clear()
                studyDays.putAll(
                    rawStudyDaysByMonth.mapValues { entry ->
                        entry.value.map { studyDayResponse ->
                            StudyDayMapper.mapToStudyDay(studyDayResponse)
                        }
                    },
                )
                updateCurrentMonthStudyDays(LocalDate.now().year, LocalDate.now().monthValue)
            }
        }

        private fun updateCurrentMonthStudyDays(
            year: Int,
            month: Int,
        ) {
            val yearMonthKey = "$year-${String.format("%02d", month)}"
            val studyDays = studyDays[yearMonthKey] ?: emptyList()
            val updatedCalendar = _mainUiState.value.calendar.copy(studyDays = studyDays)
            _mainUiState.value = _mainUiState.value.copy(calendar = updatedCalendar)
        }

        fun updateCalendarMonth(isNextMonth: Boolean) {
            val updatedCalendar =
                _mainUiState.value.calendar.apply {
                    if (isNextMonth) {
                        moveToNextMonth()
                    } else {
                        moveToPreviousMonth()
                    }
                }
            val year = updatedCalendar.getDate().year
            val month = updatedCalendar.getDate().monthValue
            updateCurrentMonthStudyDays(year, month)
        }

        fun updateTimePickerVisibility(isVisible: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isTimePickerVisible = isVisible)
        }

        fun updateTimerRunning(isTimerRunning: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isTimerRunning = isTimerRunning)
        }

        fun updateSelectedTime(selectedTime: LocalTime) {
            _mainUiState.value = _mainUiState.value.copy(selectedTime = selectedTime)
        }

        fun updateStudyRoomExtendDialogVisibility(isVisible: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isStudyRoomExtendDialog = isVisible)
        }

        fun updateStudyRoomEndDialogVisibility(isVisible: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isStudyRoomEndDialog = isVisible)
        }

        fun updateTimerStateFromTimerViewModel(timerState: TimerUiModel) {
            _mainUiState.value =
                _mainUiState.value.copy(
                    startTime = timerState.startTime,
                    endTime = timerState.endTime,
                    leftTime = timerState.leftTime,
                )
        }

        fun addStudyDay() {
            val uid = Firebase.auth.currentUser?.uid ?: return
            val startTime = _mainUiState.value.startTime
            if (uid.isNotEmpty() && startTime.isNotEmpty()) {
                viewModelScope.launch {
                    studyDayRepository.addStudyDay(uid, startTime)
                }
            }
        }
    }
