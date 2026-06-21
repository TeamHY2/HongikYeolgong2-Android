package com.teamhy2.record.data.mapper

import com.benenfeldt.remote.dto.StudyDurationResponse
import com.teamhy2.record.domain.model.StudyDuration

fun StudyDurationResponse.toDomain(): StudyDuration {
    return StudyDuration(
        yearHours = yearHours,
        yearMinutes = yearMinutes,
        monthHours = monthHours,
        monthMinutes = monthMinutes,
        dayHours = dayHours,
        dayMinutes = dayMinutes,
    )
}
