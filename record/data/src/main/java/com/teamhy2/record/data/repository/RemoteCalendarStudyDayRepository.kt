package com.teamhy2.record.data.repository

import com.benenfeldt.remote.api.StudyService
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.record.data.mapper.toDomain
import com.teamhy2.record.domain.repository.CalendarStudyDayRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class RemoteCalendarStudyDayRepository @Inject constructor(
    private val studyService: StudyService,
) : CalendarStudyDayRepository {
    private val cachedStudyDays: MutableMap<YearMonth, List<StudyDay>> = mutableMapOf()
    private val cacheMutex: Mutex = Mutex()

    override suspend fun updateCalendarStudyDay(): Result<Unit> {
        return studyService.getCalendarStudyDay().map { response ->
            val studyDays: List<StudyDay> = response.data.map { it.toDomain() }
            cacheMutex.withLock {
                cachedStudyDays.clear()
                studyDays.forEach { studyDay ->
                    val yearMonth: YearMonth = YearMonth.from(studyDay.date)
                    cachedStudyDays[yearMonth] =
                        cachedStudyDays.getOrDefault(yearMonth, emptyList()) + studyDay
                }
            }
        }
    }

    override suspend fun fetchStudyDaysForYearMonth(date: LocalDate): Result<List<StudyDay>> {
        val yearMonth: YearMonth = YearMonth.from(date)
        return cacheMutex.withLock {
            Result.success(cachedStudyDays[yearMonth] ?: emptyList())
        }
    }
}
