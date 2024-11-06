package com.teamhy2.ranking.data.mapper

import com.benenfeldt.remote.dto.WeekNumberResponse
import com.teamhy2.ranking.model.WeekNumber

fun WeekNumberResponse.toDomain(): WeekNumber {
    return WeekNumber(
        weekNumber = weekNumber,
    )
}
