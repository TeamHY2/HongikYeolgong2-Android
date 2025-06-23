package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyingUserResponse(
    val userId: Long,
    val userName: String,
    val studyDuration: String,
    val studyStatus: Boolean,
)
