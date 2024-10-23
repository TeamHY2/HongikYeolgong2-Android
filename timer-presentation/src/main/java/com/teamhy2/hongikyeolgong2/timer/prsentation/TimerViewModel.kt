package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TimerViewModel
    @Inject
    constructor(
        private val timerRepository: TimerRepository,
    ) : ViewModel() {
        private lateinit var timer: Timer

        private val _timerState = MutableStateFlow(TimerUiModel())
        val timerState: StateFlow<TimerUiModel> = _timerState.asStateFlow()

        private val _durationHour: MutableStateFlow<Duration> = MutableStateFlow(Duration.ZERO)
        val durationHour: StateFlow<Duration> = _durationHour.asStateFlow()

        init {
            getStudyRoomDuration()
        }

        private fun getStudyRoomDuration() {
            viewModelScope.launch {
                val hours = timerRepository.getStudyRoomHourDuration()
                _durationHour.value = Duration.ofHours(hours)
            }
        }

        fun setTimer(
            startTime: LocalDateTime,
            duration: Duration = durationHour.value,
            events: Map<Long, () -> Unit>,
        ) {
            timer = Timer(startTime, duration, events)
            _timerState.value =
                TimerUiModel(
                    startTime = timer.formattedStartTime,
                    startTimeMeridiem = timer.formattedStartTimeMeridiem,
                    endTime = timer.formattedEndTime,
                    endTimeMeridiem = timer.formattedEndTimeMeridiem,
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
