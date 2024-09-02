package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.StudyDayResponse

interface StudyDayRepository {
    suspend fun addStudyDay(
        uid: String,
        startTime: String,
    )

    suspend fun getStudyDaysByMonth(uid: String): Map<String, List<StudyDayResponse>>
}
