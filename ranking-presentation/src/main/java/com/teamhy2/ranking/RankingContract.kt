package com.teamhy2.ranking

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.ranking.model.DepartmentRanking

data class RankingState(
    val isLoading: Boolean = false,
    val currentWeek: String = "",
    val departmentRankings: List<DepartmentRanking> = emptyList(),
    val isNextWeekEnabled: Boolean = false,
) : UiState

sealed interface RankingIntent : UiIntent {
    data object EnterRankingScreen : RankingIntent

    data object MoveToNextMonth : RankingIntent

    data object MoveToPreviousMonth : RankingIntent
}

sealed interface RankingSideEffect : SideEffect {
    data class ShowError(val throwable: Throwable) : RankingSideEffect
}
