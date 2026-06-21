package com.teamhy2.record.domain.model

data class StudyDuration(
    val yearHours: Int,
    val yearMinutes: Int,
    val monthHours: Int,
    val monthMinutes: Int,
    val dayHours: Int,
    val dayMinutes: Int,
) {
    companion object {
        val DEFAULT =
            StudyDuration(
                yearHours = 0,
                yearMinutes = 0,
                monthHours = 0,
                monthMinutes = 0,
                dayHours = 0,
                dayMinutes = 0,
            )
    }
}
