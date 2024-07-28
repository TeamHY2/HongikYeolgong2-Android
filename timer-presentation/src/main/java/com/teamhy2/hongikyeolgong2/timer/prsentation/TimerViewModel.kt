package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.usecase.TimerUseCase
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime

class TimerViewModel : ViewModel() {
    private lateinit var timerUseCase: TimerUseCase

    var remainingTime by mutableStateOf("")
        private set

    var remainingMinutes by mutableStateOf(0L)
        private set

    var formattedStartTime by mutableStateOf("")
        private set

    var formattedEndTime by mutableStateOf("")
        private set

    fun setTimer(
        startTime: LocalTime,
        duration: Duration,
    ) {
        val timer = Timer(startTime, duration)
        timerUseCase = TimerUseCase(timer)
        formattedStartTime = timerUseCase.startTime
        formattedEndTime = timerUseCase.endTime
        remainingTime = timerUseCase.formattedRemainingTime
        remainingMinutes = timerUseCase.remainingMinutes
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            timerUseCase.timerEvents().collect { minutes ->
                remainingTime = timerUseCase.formattedRemainingTime
                remainingMinutes = minutes
            }
        }
    }
}
