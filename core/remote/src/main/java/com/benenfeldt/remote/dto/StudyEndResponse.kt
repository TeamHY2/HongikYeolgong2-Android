package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyEndResponse(
    val endTime: String,
    val studySessionId: Long,
    val userId: Long,
)
