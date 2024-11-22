package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyRoomHourDurationResponse(
    val id: Int,
    val libraryHours: Int,
)
