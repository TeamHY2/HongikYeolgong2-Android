package com.teamhy2.ranking.model

data class RankingUiState(
    val loading: Boolean = false,
    val currentWeek: String,
    val departmentRankings: List<DepartmentRanking>,
)
