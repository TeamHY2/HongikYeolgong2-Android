package com.teamhy2.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import com.teamhy2.main.domain.repository.WeeklyStudyDayRepository
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val weeklyStudyDayRepository: WeeklyStudyDayRepository,
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
                        async { fetchWiseSaying() },
                        async { fetchWeeklyStudyDay() },
                    ).awaitAll()
                }.onSuccess { results ->
                    val wiseSaying = results[0] as WiseSaying
                    val weeklyStudyDays = results[1] as List<WeeklyStudyDay>

                    _homeUiState.value =
                        HomeUiState.Success(
                            wiseSaying = wiseSaying,
                            weeklyStudyDays = weeklyStudyDays,
                        )
                }.onFailure { exception ->
                    _errorFlow.tryEmit(exception)
                    _homeUiState.value = HomeUiState.Error(exception.message)
                }
            }
        }

        private suspend fun fetchWiseSaying(): WiseSaying {
            return wiseSayingRepository.fetchWiseSaying().getOrElse { throw it }
        }

        private suspend fun fetchWeeklyStudyDay(): List<WeeklyStudyDay> {
            return weeklyStudyDayRepository.fetchWeeklyStudyDay().getOrElse { throw it }
        }

        fun updateTimerStateFromTimerViewModel(timerState: TimerUiModel) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> {
                        currentState.copy(
                            timerUiModel = timerState,
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
