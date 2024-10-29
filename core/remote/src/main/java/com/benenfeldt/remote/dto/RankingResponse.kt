package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    val weekName: String,
    val departmentRankings: List<DepartmentRankingResponse>,
)
