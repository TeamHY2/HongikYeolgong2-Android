package com.teamhy2.main.domain.model

data class StudyingUser(
    val userId: Long,
    val userName: String,
    val studyDuration: String,
    val studyStatus: Boolean,
)
