package com.teamhy2.record.data.mapper

import com.benenfeldt.remote.dto.CalendarStudyDayResponse
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import java.time.LocalDate

fun CalendarStudyDayResponse.toDomain(): StudyDay {
    return StudyDay(
        date = LocalDate.parse(date),
        studyRoomUsage = studyCount.toStudyRoomUsage(),
    )
}

internal fun Int.toStudyRoomUsage(): StudyRoomUsage {
    return when (this) {
        0 -> StudyRoomUsage.NEVER_USED
        1 -> StudyRoomUsage.USED_ONCE
        2 -> StudyRoomUsage.USED_ONCE_EXTENDED_ONCE
        3 -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
        else -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
    }
}
