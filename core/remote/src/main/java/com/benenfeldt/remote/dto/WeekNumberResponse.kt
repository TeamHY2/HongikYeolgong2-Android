package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeekNumberResponse(
    val weekNumber: Int,
)
