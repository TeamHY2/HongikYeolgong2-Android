package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TimerViewModel
    @Inject
    constructor() : ViewModel() {
        private lateinit var timer: Timer

        private val _timerState = MutableStateFlow(TimerUiModel())
        val timerState: StateFlow<TimerUiModel> = _timerState

        fun setTimer(
            startTime: LocalTime,
            duration: Duration,
            events: Map<Long, () -> Unit>,
        ) {
            timer = Timer(startTime, duration, events)
            _timerState.value =
                TimerUiModel(
                    startTime = timer.formattedStartTime,
                    endTime = timer.formattedEndTime,
                    leftTime = timer.formattedLeftTime,
                    isRunning = true,
                )
            startTimer()
        }

        private fun startTimer() {
            viewModelScope.launch {
                timer.emitTimerEvents().collect {
                    _timerState.value =
                        _timerState.value.copy(
                            leftTime = timer.formattedLeftTime,
                        )
                }
            }
        }
    }
