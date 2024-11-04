package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.WeeklyStudyDay

interface StudyDayRepository {
    suspend fun fetchWeeklyStudyDay(): Result<List<WeeklyStudyDay>>
}
