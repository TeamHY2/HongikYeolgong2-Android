package com.teamhy2.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import com.teamhy2.main.domain.repository.PromotionRepository
import com.teamhy2.main.domain.repository.StudyDayRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val studyDayRepository: StudyDayRepository,
        private val promotionRepository: PromotionRepository,
    ) : ViewModel() {
        private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        init {
            loadPromotionData()
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

        private fun loadPromotionData() {
            viewModelScope.launch {
                runCatching {
                    val promotion: Promotion = promotionRepository.fetchPromotionData().getOrThrow()
                    val isDismissedFlow: Flow<Boolean> = promotionRepository.isPromotionDismissed

                    isDismissedFlow.collect { isDismissed ->
                        _homeUiState.update { currentState ->
                            if (currentState is HomeUiState.Success) {
                                Log.d("bandal", "loadPromotionData: isDismissed = $isDismissed promotion = $promotion")
                                currentState.copy(
                                    promotion = promotion,
                                    isPromotionDialog = !isDismissed && promotion.isActive,
                                )
                            } else {
                                currentState
                            }
                        }
                    }
                }.onFailure { exception ->
                    _errorFlow.emit(exception)
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

        fun updatePromotionDismissPeriod(
            startDate: LocalDate,
            endDate: LocalDate,
        ) {
            viewModelScope.launch {
                promotionRepository.savePromotionDismissPeriod(startDate, endDate)
                    .onFailure { exception ->
                        _errorFlow.emit(exception)
                    }
            }
        }

        fun updatePromotionDialogVisibility(isVisible: Boolean) {
            _homeUiState.update { currentState ->
                when (currentState) {
                    is HomeUiState.Success -> currentState.copy(isPromotionDialog = isVisible)
                    else -> currentState
                }
            }
        }
    }
