package com.teamhy2.ranking.model

data class DepartmentRanking(
    val rank: Int,
    val departmentName: String,
    val weeklyStudyTime: Int,
    val rankChange: Int,
)
