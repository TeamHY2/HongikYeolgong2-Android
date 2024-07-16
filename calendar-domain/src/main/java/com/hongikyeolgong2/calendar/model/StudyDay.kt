package com.hongikyeolgong2.calendar.model

import java.time.LocalDate

data class StudyDay(
    val date: LocalDate,
    val studyRoomUsage: StudyRoomUsage,
)
