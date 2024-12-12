package com.teamhy2.hongikyeolgong2.timer.presentation.model

import java.time.Duration
import java.time.LocalDateTime

sealed interface TimerUiState {
    data object Idle : TimerUiState

    data class Running(
        val startDateTime: LocalDateTime,
        val startTime: TimerTime,
        val endTime: TimerTime,
        val leftTime: LeftTime,
        val duration: Duration,
    ) : TimerUiState
}
