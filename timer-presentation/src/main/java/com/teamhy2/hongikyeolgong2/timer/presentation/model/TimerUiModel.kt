package com.teamhy2.hongikyeolgong2.timer.presentation.model

data class TimerUiModel(
    val startTime: String = "",
    val startTimeMeridiem: String = "",
    val endTime: String = "",
    val endTimeMeridiem: String = "",
    val leftTime: String = "",
    val isRunning: Boolean = false,
)
