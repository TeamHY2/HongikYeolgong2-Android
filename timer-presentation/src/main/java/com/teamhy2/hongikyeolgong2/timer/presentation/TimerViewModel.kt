package com.teamhy2.hongikyeolgong2.timer.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.model.TimerDuration
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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

        private val _timerState = MutableStateFlow(TimerUiState())
        val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            initTimer()
        }

        private fun initTimer() {
            viewModelScope.launch {
                val studyRoomDurationDeferred: Deferred<Long> =
                    async { timerRepository.getStudyRoomHourDuration().toLong() }
                val timerDurationDeferred: Deferred<TimerDuration?> =
                    async { timerRepository.getCurrentTimerDuration() }

                // FIXME: val studyRoomDuration: Long = studyRoomDurationDeferred.await()
                val studyRoomDuration: Long = 1L
                val timerDuration: TimerDuration? = timerDurationDeferred.await()

                Log.d("bandal", "studyRoomDuration: $studyRoomDuration")
                Log.d("bandal", "timerDuration: $timerDuration")

                _timerState.update { state ->
                    state.copy(duration = Duration.ofHours(studyRoomDuration))
                }

                if (timerDuration == null) {
                    timerRepository.clearCurrentTimerDuration()
                    return@launch
                }

//                setTimer(
//                    startDateTime = timerDuration.startTime,
//                    duration = Duration.ofHours(studyRoomDuration),
//                    events = mapOf(Timer.TIME_OVER to {}),
//                )
            }
        }

        fun setTimer(
            startDateTime: LocalDateTime,
            duration: Duration = timerState.value.duration,
            events: Map<Long, () -> Unit>,
        ) {
            Log.d("bandal", "setTimer: 호출")
            timer = Timer(startDateTime, duration, events)
            timerJob?.cancel()
            _timerState.value =
                TimerUiState(
                    startDateTime = startDateTime,
                    startTime = timer.formattedStartTime,
                    startTimeMeridiem = timer.formattedStartTimeMeridiem,
                    endTime = timer.formattedEndTime,
                    endTimeMeridiem = timer.formattedEndTimeMeridiem,
                    leftTime = timer.formattedLeftTime,
                    duration = duration,
                    isRunning = true,
                )
            startTimer()
            timerService.startService(startDateTime, startDateTime.plus(duration))

            viewModelScope.launch {
                if (timerRepository.getCurrentTimerDuration() == null) {
                    saveCurrentTimerDuration(startDateTime, duration)
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

        private fun startTimer() {
            if (timer == Timer.IDLE) return

            timerJob =
                viewModelScope.launch {
                    timer.emitTimerEvents().collect { timeLeft ->
                        _timerState.update {
                            _timerState.value.copy(
                                leftTime = timer.formattedLeftTime,
                            )
                        }
                        if (timeLeft <= 0) {
                            timer = Timer.IDLE
                            timerJob?.cancel()
                            timerJob = null
                            _timerState.update { state ->
                                state.copy(
                                    isRunning = false,
                                )
                            }
                            timerRepository.clearCurrentTimerDuration()
                        }
                    }
                }
        }

        fun stopTimer() {
            timerService.stopService()
            timerJob?.cancel()
            timerJob = null
            timer = Timer.IDLE
            _timerState.update {
                it.copy(isRunning = false)
            }
            viewModelScope.launch {
                timerRepository.clearCurrentTimerDuration()
            }
        }
    }
