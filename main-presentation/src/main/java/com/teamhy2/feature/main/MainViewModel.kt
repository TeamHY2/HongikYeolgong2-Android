package com.teamhy2.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
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
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val studyDayRepository: StudyDayRepository,
    ) : ViewModel() {
        private val today = LocalDate.now()
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
                updateCurrentMonthStudyDays(today.year, today.monthValue)

                val yearMonthKey = "${today.year}-${String.format("%02d", today.monthValue)}"
                val todayStudyDay = studyDays[yearMonthKey]?.find { it.date == today }

                _mainUiState.value =
                    _mainUiState.value.copy(
                        starCount = calculateTodayStarCount(todayStudyDay),
                    )
            }
        }

        private fun updateCurrentMonthStudyDays(
            year: Int,
            month: Int,
        ) {
            val yearMonthKey = "$year-${String.format("%02d", month)}"
            val studyDaysForMonth = studyDays[yearMonthKey] ?: emptyList()
            val updatedCalendar = _mainUiState.value.calendar.copy(studyDays = studyDaysForMonth)
            _mainUiState.value = _mainUiState.value.copy(calendar = updatedCalendar)
        }

        fun updateTodayStudyCount() {
            val yearMonthKey = "${today.year}-${String.format("%02d", today.monthValue)}"
            val studyDaysForMonth = studyDays[yearMonthKey]?.toMutableList() ?: mutableListOf()

            val todayStudyDayIndex = studyDaysForMonth.indexOfFirst { it.date == today }
            if (todayStudyDayIndex != -1) {
                val updatedStudyRoomUsage =
                    StudyDayMapper.getNextStudyRoomUsage(studyDaysForMonth[todayStudyDayIndex].studyRoomUsage)
                studyDaysForMonth[todayStudyDayIndex] =
                    studyDaysForMonth[todayStudyDayIndex].copy(
                        studyRoomUsage = updatedStudyRoomUsage,
                    )
            } else {
                studyDaysForMonth.add(
                    StudyDay(date = today, studyRoomUsage = StudyRoomUsage.USED_ONCE),
                )
            }

            studyDays[yearMonthKey] = studyDaysForMonth

            val starCount = calculateTodayStarCount(studyDaysForMonth.find { it.date == today })

            if (mainUiState.value.calendar.date.year == today.year && mainUiState.value.calendar.date.monthValue == today.monthValue) {
                val updatedCalendar = _mainUiState.value.calendar.copy(studyDays = studyDaysForMonth)
                _mainUiState.value =
                    _mainUiState.value.copy(
                        calendar = updatedCalendar,
                        starCount = starCount,
                    )
            } else {
                _mainUiState.value =
                    _mainUiState.value.copy(
                        starCount = starCount,
                    )
            }
        }

        private fun calculateTodayStarCount(todayStudyDay: StudyDay?): Int {
            return StudyDayMapper.mapStudyRoomUsageToStarCount(
                todayStudyDay?.studyRoomUsage ?: StudyRoomUsage.NEVER_USED,
            )
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
            val year = updatedCalendar.date.year
            val month = updatedCalendar.date.monthValue
            updateCurrentMonthStudyDays(year, month)
        }

        fun updateTimePickerVisibility(isVisible: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isTimePickerVisible = isVisible)
        }

        fun updateTimerRunning(isTimerRunning: Boolean) {
            _mainUiState.value = _mainUiState.value.copy(isTimerRunning = isTimerRunning)
        }

        fun updateSelectedTime(selectedTime: LocalDateTime) {
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
                    startTimeMeridiem = timerState.startTimeMeridiem,
                    endTimeMeridiem = timerState.endTimeMeridiem,
                )
        }

        fun addStudyDay() {
            val uid = Firebase.auth.currentUser?.uid ?: return
            var startTime = _mainUiState.value.startTime
            val startTimeMeridiem = _mainUiState.value.startTimeMeridiem

            val timeParts = startTime.split(":").map { it.toInt() }
            var hour = timeParts[0]
            val minute = timeParts[1]

            if (startTimeMeridiem == "PM") {
                hour += 12
            }
            val adjustedStartTime = LocalTime.of(hour, minute)

            if (uid.isNotEmpty() && startTime.isNotEmpty()) {
                viewModelScope.launch {
                    studyDayRepository.addStudyDay(uid, adjustedStartTime.toString())
                }
            }
        }
    }
