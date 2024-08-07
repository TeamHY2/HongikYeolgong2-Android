package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
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

        private val _startTime = MutableStateFlow("")
        val startTime: StateFlow<String> = _startTime

        private val _endTime = MutableStateFlow("")
        val endTime: StateFlow<String> = _endTime

        private val _leftTime = MutableStateFlow("")
        val leftTime: StateFlow<String> = _leftTime

        fun setTimer(
            startTime: LocalTime,
            duration: Duration,
            events: Map<Long, () -> Unit>,
        ) {
            timer = Timer(startTime, duration, events)
            _startTime.value = timer.formattedStartTime
            _endTime.value = timer.formattedEndTime
            _leftTime.value = timer.formattedLeftTime
            startTimer()
        }

        private fun startTimer() {
            viewModelScope.launch {
                timer.emitTimerEvents().collect {
                    _leftTime.value = timer.formattedLeftTime
                }
            }
        }
    }
