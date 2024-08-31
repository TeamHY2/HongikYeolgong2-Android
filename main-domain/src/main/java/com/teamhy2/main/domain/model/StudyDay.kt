package com.teamhy2.main.domain.model

import java.time.LocalDate

data class StudyDay(
    val date: LocalDate,
    val secondDuration: Long,
) {
    companion object {
        const val DATE_KEY = "date"
        const val SECOND_DURATION_KEY = "secondDuration"
    }
}

fun StudyDay.toMap(): Map<String, Any> {
    return mapOf(
        StudyDay.DATE_KEY to this.date.toString(),
        StudyDay.SECOND_DURATION_KEY to this.secondDuration,
    )
}
