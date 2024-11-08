package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CalendarStudyDayResponse(
    val date: String,
    val studyCount: Int,
)
