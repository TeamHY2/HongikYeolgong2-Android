package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.StudyDayRecord
import com.teamhy2.main.domain.model.WeeklyStudyDay
import java.time.LocalDateTime

interface StudyDayRepository {
    suspend fun fetchWeeklyStudyDay(): Result<List<WeeklyStudyDay>>

    suspend fun saveStudyDay(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Result<StudyDayRecord>
}
