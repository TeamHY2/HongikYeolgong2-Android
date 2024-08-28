package com.teamhy2.main.model

import java.time.LocalDate

data class StudyDay(
    val date: LocalDate,
    val minuteDuration: Long,
)

fun StudyDay.toMap(): Map<String, Any> {
    return mapOf(
        "date" to this.date.toString(),
        "minuteDuration" to this.minuteDuration,
    )
}
