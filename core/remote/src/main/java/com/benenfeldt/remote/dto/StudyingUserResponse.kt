package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyingUserResponse(
    val userId: Long,
    @SerialName("nickname")
    val userName: String,
    val studyDuration: String,
    val studyStatus: Boolean,
)
