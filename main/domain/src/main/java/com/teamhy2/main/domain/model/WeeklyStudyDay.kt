package com.teamhy2.main.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WeeklyStudyDay(
    val date: String,
    val studyCount: Int,
) {
    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("M/dd")

        fun defaultWeek(): List<WeeklyStudyDay> {
            val today: LocalDate = LocalDate.now()
            val startOfWeek: LocalDate = today.with(DayOfWeek.MONDAY)
            val weekRange: IntRange = 0 until 7

            return weekRange.map { offset: Int ->
                val date: String = startOfWeek.plusDays(offset.toLong()).format(formatter)
                WeeklyStudyDay(date = date, studyCount = 0)
            }
        }
    }
}
