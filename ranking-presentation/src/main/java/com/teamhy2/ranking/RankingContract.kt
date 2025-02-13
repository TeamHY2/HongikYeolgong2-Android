package com.teamhy2.ranking

import com.teamhy2.ranking.model.DepartmentRanking

data class RankingState(
    val isLoading: Boolean = false,
    val currentWeek: String = "",
    val departmentRankings: List<DepartmentRanking> = emptyList(),
)

sealed interface RankingSideEffect {
    data class ShowError(val throwable: Throwable) : RankingSideEffect
}
