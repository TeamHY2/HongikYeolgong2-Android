package com.teamhy2.ranking.model

data class Ranking(
    val weekName: String,
    val departmentRankings: List<DepartmentRanking>,
)
