package com.teamhy2.main.domain

interface StudyDayRepository {
    suspend fun addStudyDay(
        uid: String,
        startTime: String,
    )
}
