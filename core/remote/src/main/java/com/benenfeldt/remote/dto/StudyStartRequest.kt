package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyStartRequest(
    val startTime: String,
)
