package com.teamhy2.ranking.model

sealed interface RankingUiState {
    object Loading : RankingUiState

    data class Success(
        val currentWeek: String,
        val departmentRankings: List<DepartmentRanking>,
    ) : RankingUiState

    data class Error(val message: String) : RankingUiState
}
