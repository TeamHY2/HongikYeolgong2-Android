package com.teamhy2.feature.main.model

import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.main.domain.model.WiseSaying
import java.time.LocalDateTime

data class MainUiState(
    val isTimerRunning: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isStudyRoomExtendDialog: Boolean = false,
    val isStudyRoomEndDialog: Boolean = false,
    val wiseSaying: WiseSaying = WiseSaying("", ""),
    val selectedTime: LocalDateTime = LocalDateTime.now(),
    val calendar: Calendar = Calendar(studyDays = emptyList()),
    val startTime: String = "",
    val startTimeMeridiem: String = "",
    val endTime: String = "",
    val endTimeMeridiem: String = "",
    val leftTime: String = "",
    val starCount: Int = 0,
)
