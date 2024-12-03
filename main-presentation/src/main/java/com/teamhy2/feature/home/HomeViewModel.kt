package com.teamhy2.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import com.teamhy2.main.domain.repository.StudyDayRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val studyDayRepository: StudyDayRepository,
        private val timerService: TimerService,
    ) : ViewModel() {
        private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            loadHomeData()
        }

        private fun loadHomeData() {
            viewModelScope.launch {
                runCatching {
                    listOf(
                        async { wiseSayingRepository.fetchWiseSaying().getOrThrow() },
                        async { studyDayRepository.fetchWeeklyStudyDay().getOrThrow() },
                    ).awaitAll()
                }.onSuccess { results ->
                    val (wiseSaying, weeklyStudyDays) = results

                    _homeUiState.update {
                        HomeUiState.Success(
                            wiseSaying = wiseSaying as WiseSaying,
                            weeklyStudyDays = weeklyStudyDays as List<WeeklyStudyDay>,
                        )
                    }
                }.onFailure { exception ->
                    _errorFlow.emit(exception)
                    _homeUiState.value = HomeUiState.Error(exception.message)
                }
            }
        }

        fun startTimerService(
            startDateTime: LocalDateTime,
            duration: Duration,
        ) {
            timerService.startService(startDateTime, duration)
        }

        fun stopTimerService() {
            timerService.stopService()
        }

        fun saveStudyDay(isExtend: Boolean) {
            val currentState = _homeUiState.value
            if (currentState is HomeUiState.Success) {
                viewModelScope.launch {
                    studyDayRepository.saveStudyDay(
                        startDateTime = currentState.timerUiState.startDateTime,
                        endDateTime = LocalDateTime.now(),
                    ).onSuccess {
                        if (!isExtend) loadHomeData()
                    }.onFailure { throwable ->
                        _errorFlow.emit(throwable)
                    }
                }
            }
        }

        fun increaseTodayStudyCount() {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> {
                        val updatedWeeklyStudyDays: List<WeeklyStudyDay> =
                            currentState.weeklyStudyDays.map { studyDay ->
                                if (studyDay.date ==
                                    LocalDate.now()
                                        .format(DateTimeFormatter.ofPattern("M/dd"))
                                ) {
                                    studyDay.copy(studyCount = studyDay.studyCount + 1)
                                } else {
                                    studyDay
                                }
                            }
                        currentState.copy(weeklyStudyDays = updatedWeeklyStudyDays)
                    }

                    else -> currentState
                }
            }
        }

        fun updateTimerStateFromTimerViewModel(timerState: TimerUiState) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> {
                        currentState.copy(
                            timerUiState = timerState,
                        )
                    }

                    else -> currentState
                }
            }
        }

        fun updateTimePickerVisibility(isVisible: Boolean) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(isTimePickerVisible = isVisible)
                    else -> currentState
                }
            }
        }

        fun updateTimerRunning(isTimerRunning: Boolean) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(isTimerRunning = isTimerRunning)
                    else -> currentState
                }
            }
        }

        fun updateSelectedTime(selectedTime: LocalDateTime) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(selectedTime = selectedTime)
                    else -> currentState
                }
            }
        }

        fun updateStudyRoomExtendDialogVisibility(isVisible: Boolean) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(isStudyRoomExtendDialog = isVisible)
                    else -> currentState
                }
            }
        }

        fun updateStudyRoomEndDialogVisibility(isVisible: Boolean) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(isStudyRoomEndDialog = isVisible)
                    else -> currentState
                }
            }
        }
    }
