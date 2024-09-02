package com.teamhy2.feature.main.model

import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.main.domain.model.WiseSaying
import java.time.LocalTime

data class MainUiState(
    val isTimerRunning: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isStudyRoomExtendDialog: Boolean = false,
    val isStudyRoomEndDialog: Boolean = false,
    val wiseSaying: WiseSaying = WiseSaying("", ""),
    val selectedTime: LocalTime = LocalTime.now(),
    val calendar: Calendar = Calendar(studyDays = emptyList()),
    val startTime: String = "",
    val endTime: String = "",
    val leftTime: String = "",
)
