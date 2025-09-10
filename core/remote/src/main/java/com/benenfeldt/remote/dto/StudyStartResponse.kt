package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyStartResponse(
    @SerialName("studySessionId")
    val id: Long,
    val startTime: String,
    val userId: Long,
)
