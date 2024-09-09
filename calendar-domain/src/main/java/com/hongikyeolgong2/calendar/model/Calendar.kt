package com.hongikyeolgong2.calendar.model

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class Calendar(
    initDate: LocalDate = LocalDate.now(),
    private val dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_PATTERN).withLocale(Locale.ENGLISH),
    private val studyDays: List<StudyDay> = emptyList(),
) {
    var date: LocalDate = initDate
        private set

    val now: String
        get() = dateTimeFormatter.format(date)

    fun getMonth(): List<StudyDay> {
        val studyDaysWithMonth: List<StudyDay> = getStudyDaysByMonth()
        val existingDays: Map<Int, StudyDay> = studyDaysWithMonth.associateBy { it.date.dayOfMonth }

        return (1..getLastDayOfMonth()).map { day ->
            existingDays[day] ?: StudyDay(
                date = date.withDayOfMonth(day),
                studyRoomUsage = StudyRoomUsage.NEVER_USED,
            )
        }.sortedBy { it.date.dayOfMonth }
    }

    fun getStudyDaysByMonth(): List<StudyDay> {
        return studyDays.filter { it.date.month == date.month }
    }

    fun getLastDayOfMonth(): Int {
        return YearMonth.from(date).atEndOfMonth().dayOfMonth
    }

    fun moveToPreviousMonth() {
        date = date.minusMonths(1)
    }

    fun moveToNextMonth() {
        date = date.plusMonths(1)
    }

    fun copy(
        initDate: LocalDate = this.date,
        studyDays: List<StudyDay> = this.studyDays,
    ): Calendar {
        return Calendar(
            initDate = initDate,
            dateTimeFormatter = this.dateTimeFormatter,
            studyDays = studyDays,
        )
    }

    companion object {
        private const val DEFAULT_DATE_TIME_FORMAT_PATTERN = "MMM yyyy"
    }
}
