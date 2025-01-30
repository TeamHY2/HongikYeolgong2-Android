package com.teamhy2.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.ranking.model.RankingUiState
import com.teamhy2.ranking.repository.RankingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RankingViewModel
    @Inject
    constructor(
        private val rankingRepository: RankingRepository,
    ) : ViewModel() {
        private val _rankingUiState = MutableStateFlow<RankingUiState>(RankingUiState.Loading)
        val rankingUiState: StateFlow<RankingUiState>
            get() = _rankingUiState.asStateFlow()

        private val _errorFlow = MutableSharedFlow<Throwable>()
        val errorFlow: SharedFlow<Throwable> = _errorFlow.asSharedFlow()

        private var currentWeekNumber: Int? = null
        private var latestWeekNumber: Int? = null

        init {
            getWeekNumber()
        }

        private fun getWeekNumber(date: LocalDate = LocalDate.now()) {
            viewModelScope.launch {
                rankingRepository.fetchWeekNumber(date)
                    .onSuccess { weekNumber ->
                        currentWeekNumber = weekNumber.weekNumber
                        latestWeekNumber = weekNumber.weekNumber
                        getDepartmentRankings(weekNumber.weekNumber)
                    }
                    .onFailure { throwable ->
                        _errorFlow.emit(throwable)
                    }
            }
        }

        private fun getDepartmentRankings(weekNumber: Int) {
            viewModelScope.launch {
                rankingRepository.fetchRanking(weekNumber)
                    .onSuccess { ranking ->
                        currentWeekNumber = weekNumber
                        _rankingUiState.update {
                            RankingUiState.Success(
                                currentWeek = ranking.weekName,
                                departmentRankings = ranking.departmentRankings,
                            )
                        }
                    }
                    .onFailure { throwable ->
                        _errorFlow.emit(throwable)
                    }
            }
        }

        fun getLastWeekRanking() {
            currentWeekNumber?.let { currentWeekNumber ->
                val week = currentWeekNumber % 100
                if (week == 1) {
                    return
                }

                getDepartmentRankings(currentWeekNumber - 1)
            }
        }

        fun getNextWeekRanking() {
            currentWeekNumber?.let { currentWeekNumber ->
                if (currentWeekNumber >= (latestWeekNumber ?: currentWeekNumber)) {
                    return
                }
                getDepartmentRankings(currentWeekNumber + 1)
            }
        }
    }
