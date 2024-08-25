package com.teamhy2.feature.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.main.domain.WebViewRepository
import com.teamhy2.main.domain.WiseSayingRepository
import com.teamhy2.onboarding.domain.repository.UserRepository
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
        private val webViewRepository: WebViewRepository,
        private val wiseSayingRepository: WiseSayingRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _mainUiState = MutableStateFlow(MainUiState())
        val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

        var urls by mutableStateOf<Map<String, String>>(emptyMap())
            private set

        private val _userExists: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

        init {
            getFirebaseUrls()
            getWiseSaying()
            getCalendarData()
        }

        fun checkUserExists(uid: String?) {
            if (uid == null) {
                _userExists.value = false
                return
            }

            viewModelScope.launch {
                _userExists.value = userRepository.checkUserExists(uid)
                Log.d("Bandal", "checkUserExists: ${_userExists.value}")
            }
        }

        private fun getFirebaseUrls() {
            viewModelScope.launch {
                val urlList = webViewRepository.fetchFirebaseUrls()
                urls = urlList.toMap()
            }
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
            val initialCalendar =
                Calendar(
                    studyDays =
                        listOf(
                            StudyDay(
                                date = LocalDate.now().withDayOfMonth(1),
                                studyRoomUsage = StudyRoomUsage.USED_ONCE,
                            ),
                        ),
                )
            _mainUiState.value = mainUiState.value.copy(calendar = initialCalendar)
        }

        fun updateTimePickerVisibility(isVisible: Boolean) {
            _mainUiState.value = mainUiState.value.copy(isTimePickerVisible = isVisible)
        }

        fun updateTimerRunning(isTimerRunning: Boolean) {
            _mainUiState.value = mainUiState.value.copy(isTimerRunning = isTimerRunning)
        }

        fun updateSelectedTime(selectedTime: LocalTime) {
            _mainUiState.value = mainUiState.value.copy(selectedTime = selectedTime)
        }

        fun updateCalendarMonth(isNextMonth: Boolean) {
            val updatedCalendar =
                mainUiState.value.calendar.apply {
                    if (isNextMonth) {
                        moveToNextMonth()
                    } else {
                        moveToPreviousMonth()
                    }
                }
            _mainUiState.value = mainUiState.value.copy(calendar = updatedCalendar)
        }
    }
