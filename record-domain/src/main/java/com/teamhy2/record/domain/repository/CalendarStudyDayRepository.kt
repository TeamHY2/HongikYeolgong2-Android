package com.teamhy2.record.domain.repository

import com.hongikyeolgong2.calendar.model.StudyDay
import java.time.LocalDate

interface CalendarStudyDayRepository {
    suspend fun updateCalendarStudyDay(): Result<Unit>

    suspend fun fetchStudyDaysForYearMonth(date: LocalDate): Result<List<StudyDay>>
}
