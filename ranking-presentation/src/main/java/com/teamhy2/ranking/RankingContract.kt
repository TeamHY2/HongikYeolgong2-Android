package com.teamhy2.ranking

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.ranking.model.DepartmentRanking
import kotlinx.collections.immutable.ImmutableList

sealed interface RankingUiState : UiState {
    data object Loading : RankingUiState

    data class Loaded(
        val currentWeek: String,
        val departmentRankings: ImmutableList<DepartmentRanking>,
        val canMoveToPreviousWeek: Boolean,
        val canMoveToNextWeek: Boolean,
    ) : RankingUiState
}

sealed interface RankingIntent : UiIntent {
    data object EnterRankingScreen : RankingIntent

    data object MoveToNextMonth : RankingIntent

    data object MoveToPreviousMonth : RankingIntent
}

sealed interface RankingSideEffect : SideEffect {
    data class ShowError(val throwable: Throwable) : RankingSideEffect
}
