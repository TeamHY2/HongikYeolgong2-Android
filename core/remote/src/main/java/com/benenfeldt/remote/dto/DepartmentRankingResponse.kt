package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentRankingResponse(
    val department: String,
    val studyDurationOfWeek: Int,
    val currentRank: Int,
    val rankChange: Int,
)
