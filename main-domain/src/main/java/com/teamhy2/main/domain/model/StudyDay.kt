package com.teamhy2.main.domain.model

import java.time.LocalDate

data class StudyDay(
    val date: LocalDate,
    val secondDuration: Long,
)

fun StudyDay.toMap(): Map<String, Any> {
    return mapOf(
        "date" to this.date.toString(),
        "secondDuration" to this.secondDuration,
    )
}
