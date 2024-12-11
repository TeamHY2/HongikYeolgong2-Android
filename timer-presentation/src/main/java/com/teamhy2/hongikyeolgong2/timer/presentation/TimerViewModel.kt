package com.teamhy2.hongikyeolgong2.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.model.TimerDuration
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import com.teamhy2.hongikyeolgong2.timer.presentation.model.LeftTime
import com.teamhy2.hongikyeolgong2.timer.presentation.model.Time
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TimerViewModel
    @Inject
    constructor(
        private val timerRepository: TimerRepository,
        private val timerService: TimerService,
    ) : ViewModel() {
        private var timer: Timer = Timer.IDLE
        private var timerJob: Job? = null

        private val _timerState: MutableStateFlow<TimerUiState> = MutableStateFlow(TimerUiState.Idle)
        val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        private val studyRoomDuration: MutableStateFlow<Int> =
            MutableStateFlow(TimerRepository.MINIMUM_STUDY_ROOM_HOUR_DURATION)

        init {
            viewModelScope.launch {
                studyRoomDuration.value = timerRepository.getStudyRoomHourDuration()
            }
            initTimer()
        }

        private fun initTimer() {
            viewModelScope.launch {
                val timerDuration: TimerDuration? = timerRepository.getCurrentTimerDuration()

                if (timerDuration == null) {
                    timerRepository.clearCurrentTimerDuration()
                    return@launch
                }

                setTimer(
                    isAlreadyRunning = true,
                    startDateTime = timerDuration.startTime,
                    endDateTime = timerDuration.endTime,
                )
            }
        }

        fun setTimer(
            isAlreadyRunning: Boolean = false,
            startDateTime: LocalDateTime,
            endDateTime: LocalDateTime? = null,
        ) {
            val duration: Duration = Duration.ofHours(studyRoomDuration.value.toLong())
            timer =
                if (isAlreadyRunning) {
                    Timer(
                        startTime = startDateTime,
                        duration = duration,
                        endTime = endDateTime ?: return,
                    )
                } else {
                    Timer(startTime = startDateTime, duration = duration)
                }
            timerJob?.cancel()
            val startTime: Time = Time.create(timer.startTime)
            val endTime: Time = Time.create(timer.endTime)
            val leftTime: LeftTime = LeftTime.create(timer.endTime)

            _timerState.update {
                TimerUiState.Running(
                    startDateTime = startDateTime,
                    startTime = startTime,
                    endTime = endTime,
                    leftTime = leftTime,
                    duration = Duration.between(startDateTime, timer.endTime),
                )
            }

            startTimer()

            if (isAlreadyRunning.not()) {
                timerService.startService(
                    startDateTime = startDateTime,
                    endDateTime = startDateTime.plus(duration),
                )
                viewModelScope.launch {
                    if (timerRepository.getCurrentTimerDuration() == null) {
                        saveCurrentTimerDuration(startDateTime, duration)
                    }
                }
            }
        }

        private fun startTimer() {
            if (timer == Timer.IDLE) return

            timerJob =
                viewModelScope.launch {
                    timer.emitTimerEvents().collect { timeLeft ->
                        _timerState.update { state ->
                            when (state) {
                                is TimerUiState.Idle -> state
                                is TimerUiState.Running -> {
                                    state.copy(
                                        leftTime = LeftTime.create(timer.endTime),
                                    )
                                }
                            }
                        }
                        if (timeLeft <= 0) {
                            timer = Timer.IDLE
                            timerJob?.cancel()
                            timerJob = null
                            _timerState.update {
                                TimerUiState.Idle
                            }
                            timerRepository.clearCurrentTimerDuration()
                        }
                    }
                }
        }

        private fun saveCurrentTimerDuration(
            startTime: LocalDateTime,
            duration: Duration,
        ) {
            viewModelScope.launch {
                timerRepository.setCurrentTimerDuration(
                    TimerDuration(
                        startTime = startTime,
                        endTime = startTime.plus(duration),
                    ),
                )
            }
        }

        fun stopTimer() {
            timerService.stopService()
            timerJob?.cancel()
            timerJob = null
            timer = Timer.IDLE
            _timerState.update {
                TimerUiState.Idle
            }
            viewModelScope.launch {
                timerRepository.clearCurrentTimerDuration()
            }
        }

        fun extendTime() {
            timer.extend()

            val endTime: Time = Time.create(timer.endTime)

            _timerState.update { currentState ->
                (currentState as TimerUiState.Running).copy(
                    endTime = endTime,
                    duration = Duration.between(timer.startTime, timer.endTime),
                )
            }

            val startTime = LocalDateTime.now()

            viewModelScope.launch {
                timerRepository.clearCurrentTimerDuration()
                timerRepository.setCurrentTimerDuration(
                    TimerDuration(
                        startTime = startTime,
                        endTime = timer.endTime,
                    ),
                )
            }

            timerService.stopService()
            timerService.startService(
                startDateTime = startTime,
                endDateTime = timer.endTime,
            )
        }
    }
