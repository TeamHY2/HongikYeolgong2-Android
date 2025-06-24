package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyStartResponse(
    val id: Long,
    val startTime: String,
    val userId: Long,
)
