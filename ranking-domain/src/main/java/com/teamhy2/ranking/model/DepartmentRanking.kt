package com.teamhy2.ranking.model

data class DepartmentRanking(
    val department: String,
    val studyDurationOfWeek: Int,
    val currentRank: Int,
    val rankChange: Int,
)
