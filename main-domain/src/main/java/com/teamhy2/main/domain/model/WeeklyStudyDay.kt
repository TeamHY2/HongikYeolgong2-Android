package com.teamhy2.main.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WeeklyStudyDay(
    val date: String,
    val studyCount: Int,
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("M/dd")

        fun defaultWeek(): List<WeeklyStudyDay> {
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            return (0 until 7).map { offset ->
                val date = startOfWeek.plusDays(offset.toLong()).format(formatter)
                WeeklyStudyDay(date = date, studyCount = 0)
            }
        }
    }
}
