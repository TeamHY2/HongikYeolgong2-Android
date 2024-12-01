package com.teamhy2.hongikyeolgong2.timer.presentation.model

import java.time.LocalDateTime

data class TimerUiModel(
    val startDateTime: LocalDateTime = LocalDateTime.now(),
    val startTime: String = "",
    val startTimeMeridiem: String = "",
    val endTime: String = "",
    val endTimeMeridiem: String = "",
    val leftTime: String = "",
    val isRunning: Boolean = false,
)
