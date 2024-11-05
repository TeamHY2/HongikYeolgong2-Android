package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyDayRequest(
    val startTime: String,
    val endTime: String,
)
