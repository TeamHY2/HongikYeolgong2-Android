package com.hongikyeolgong2.calendar.model

import java.time.LocalDate
import java.time.YearMonth

class Calendar(
    private val date: LocalDate = LocalDate.now()
) {
    fun getLastDayOfMonth(): Int {
        return YearMonth.from(date).atEndOfMonth().dayOfMonth
    }
}
