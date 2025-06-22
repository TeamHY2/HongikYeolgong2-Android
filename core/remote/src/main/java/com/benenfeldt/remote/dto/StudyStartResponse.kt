package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyStartResponse(
    val id: Int,
    val startTime: String,
    val userId: Int,
)
