package com.teamhy2.hongikyeolgong2.timer.prsentation.model

data class TimerUiModel(
    val startTime: String = "",
    val startTimeMeridiem: String = "",
    val endTime: String = "",
    val endTimeMeridiem: String = "",
    val leftTime: String = "",
    val isRunning: Boolean = false,
)
