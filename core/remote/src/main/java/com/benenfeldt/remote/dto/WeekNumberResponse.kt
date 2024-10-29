package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeekNumberResponse(
    val year: Int,
    val weekName: String,
    val weekNumber: Int,
)
