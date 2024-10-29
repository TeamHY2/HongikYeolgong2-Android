package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    val weekName: String,
    val departmentRankings: List<DepartmentRanking>,
) {
    @Serializable
    data class DepartmentRanking(
        val department: String,
        val studyDurationOfWeek: Int,
        val currentRank: Int,
        val rankChange: Int,
    )
}
