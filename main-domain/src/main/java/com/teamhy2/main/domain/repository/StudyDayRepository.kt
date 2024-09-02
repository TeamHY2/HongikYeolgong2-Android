package com.teamhy2.main.domain.repository

interface StudyDayRepository {
    suspend fun addStudyDay(
        uid: String,
        startTime: String,
    )
}
