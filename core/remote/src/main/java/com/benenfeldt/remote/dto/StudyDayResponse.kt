package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyDayResponse(
    val id: Int,
    val userId: Int,
    val startTime: String,
    val endTime: String,
)
