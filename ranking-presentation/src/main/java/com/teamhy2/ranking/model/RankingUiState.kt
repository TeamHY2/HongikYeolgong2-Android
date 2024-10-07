package com.teamhy2.ranking.model

data class RankingUiState(
    val currentWeek: String = "",
    val departmentRankings: List<DepartmentRanking> = emptyList(),
)
