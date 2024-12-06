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

        private val studyRoomDuration: MutableStateFlow<Int> = MutableStateFlow(4)

        init {
            viewModelScope.launch {
                studyRoomDuration.value = 1
                // FIXME: studyRoomDuration.value = timerRepository.getStudyRoomHourDuration()
            }
        }

        private fun initTimer() {
//            viewModelScope.launch {
//                val timerDurationDeferred: Deferred<TimerDuration?> =
//                    async { timerRepository.getCurrentTimerDuration() }
//
//                // FIXME: val studyRoomDuration: Long = studyRoomDurationDeferred.await()
//                val studyRoomDuration: Long = 1L
//                val timerDuration: TimerDuration? = timerDurationDeferred.await()
//
//                Log.d("bandal", "studyRoomDuration: $studyRoomDuration")
//                Log.d("bandal", "timerDuration: $timerDuration")
//
//                _timerState.update { state ->
//                    when
//                    state.copy(duration = Duration.ofHours(studyRoomDuration))
//                }
//
//                if (timerDuration == null) {
//                    timerRepository.clearCurrentTimerDuration()
//                    return@launch
//                }

//                setTimer(
//                    startDateTime = timerDuration.startTime,
//                    duration = Duration.ofHours(studyRoomDuration),
//                    events = mapOf(Timer.TIME_OVER to {}),
//                )
//            }
        }

        fun setTimer(
            startDateTime: LocalDateTime,
            events: Map<Long, () -> Unit>,
        ) {
            val duration = Duration.ofHours(studyRoomDuration.value.toLong())
            timer = Timer(startDateTime, duration, events)
            timerJob?.cancel()
            val startTime = Time.create(timer.startTime)
            val endTime = Time.create(timer.endTime)
            val leftTime = LeftTime.create(timer.endTime)

            _timerState.update {
                TimerUiState.Running(
                    startDateTime = startDateTime,
                    startTime = startTime,
                    endTime = endTime,
                    leftTime = leftTime,
                    duration = duration,
                )
            }

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
    }
