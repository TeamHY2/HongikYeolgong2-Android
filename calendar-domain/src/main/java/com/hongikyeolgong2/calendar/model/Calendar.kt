package com.hongikyeolgong2.calendar.model

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class Calendar(
    initDate: LocalDate = LocalDate.now(),
    private val dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_PATTERN),
) {
    private var date: LocalDate = initDate

    val now: String
        get() = dateTimeFormatter.format(date)

    fun getLastDayOfMonth(): Int {
        return YearMonth.from(date).atEndOfMonth().dayOfMonth
    }

    fun moveToPreviousMonth() {
        date = date.minusMonths(1)
    }

    fun moveToNextMonth() {
        date = date.plusMonths(1)
    }

    companion object {
        private const val DEFAULT_DATE_TIME_FORMAT_PATTERN = "MMM yyyy"
    }
}
