package com.teamhy2.main.data.mapper

import com.benenfeldt.remote.dto.StudyDayResponse
import com.teamhy2.main.domain.model.StudyDayRecord

fun StudyDayResponse.toDomain(): StudyDayRecord {
    return StudyDayRecord(
        id = id,
        userId = userId,
        startTime = startTime,
        endTime = endTime,
    )
}
