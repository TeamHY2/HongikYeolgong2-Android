package com.teamhy2.ranking

import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.ranking.repository.RankingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class RankingViewModel
    @Inject
    constructor(
        private val rankingRepository: RankingRepository,
        private val weekNumberCalculator: WeekNumberCalculator,
    ) : MviViewModel<RankingIntent, RankingUiState, RankingSideEffect>(RankingUiState.Loading) {
        override suspend fun reduceState(
            current: RankingUiState,
            intent: RankingIntent,
        ): RankingUiState {
            return when (intent) {
                is RankingIntent.EnterRankingScreen -> getDepartmentRankings(current)
                is RankingIntent.MoveToPreviousMonth -> getPreviousWeekRanking(current)
                is RankingIntent.MoveToNextMonth -> getNextWeekRanking(current)
            }
        }

        private suspend fun getDepartmentRankings(current: RankingUiState): RankingUiState {
            return rankingRepository.fetchRanking(weekNumberCalculator.currentWeekNumber)
                .fold(
                    onSuccess = { ranking ->
                        RankingUiState.Loaded(
                            currentWeek = ranking.weekName,
                            departmentRankings = ranking.departmentRankings.toImmutableList(),
                            canMoveToPreviousWeek = weekNumberCalculator.isMinimumWeekNumber.not(),
                            canMoveToNextWeek = weekNumberCalculator.isMaximumWeekNumber.not(),
                        )
                    },
                    onFailure = { throwable ->
                        postSideEffect(RankingSideEffect.ShowError(throwable))
                        current
                    },
                )
        }

        private suspend fun getPreviousWeekRanking(current: RankingUiState): RankingUiState {
            if (weekNumberCalculator.isMinimumWeekNumber) return current
            weekNumberCalculator.moveToPreviousWeek()
            return getDepartmentRankings(current)
        }

        private suspend fun getNextWeekRanking(current: RankingUiState): RankingUiState {
            if (weekNumberCalculator.isMaximumWeekNumber) return current
            weekNumberCalculator.moveToNextWeek()
            return getDepartmentRankings(current)
        }
    }
