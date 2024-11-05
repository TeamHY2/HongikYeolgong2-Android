package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyStudyDayResponse(
    val date: String,
    val studyCount: Int,
)
