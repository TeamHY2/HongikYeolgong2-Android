package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyEndRequest(
    val studySessionId: Long,
    val endTime: String,
)
