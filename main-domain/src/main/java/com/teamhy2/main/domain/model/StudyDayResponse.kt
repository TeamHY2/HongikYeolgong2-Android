package com.teamhy2.main.domain.model

import java.time.LocalDate

data class StudyDayResponse(
    val date: LocalDate,
    val studyStartTimes: List<String>,
)
