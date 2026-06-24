package com.teamhy2.main.data.mapper

import com.benenfeldt.remote.dto.WeeklyStudyDayResponse
import com.teamhy2.main.domain.model.WeeklyStudyDay

fun WeeklyStudyDayResponse.toDomain(): WeeklyStudyDay {
    return WeeklyStudyDay(
        date = date,
        studyCount = studyCount,
    )
}

fun List<WeeklyStudyDayResponse>.toDomain(): List<WeeklyStudyDay> {
    return this.map { it.toDomain() }
}
