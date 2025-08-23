package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.StudyEndResult
import com.teamhy2.main.domain.model.StudyStartResult
import com.teamhy2.main.domain.model.StudyingUser
import com.teamhy2.main.domain.model.WeeklyStudyDay

interface StudyRepository {
    suspend fun fetchWeeklyStudyDay(): Result<List<WeeklyStudyDay>>

    suspend fun startStudy(startTime: String): Result<StudyStartResult>

    suspend fun endStudy(
        studySessionId: Long,
        endTime: String,
    ): Result<StudyEndResult>

    suspend fun getStudyingUsers(): Result<List<StudyingUser>>
}
