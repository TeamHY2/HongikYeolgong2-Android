package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyDurationResponse(
    val yearHours: Int,
    val yearMinutes: Int,
    val monthHours: Int,
    val monthMinutes: Int,
    val dayHours: Int,
    val dayMinutes: Int,
)
