package com.teamhy2.feature.main.mapper

import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import com.teamhy2.main.domain.model.StudyDayResponse

object StudyDayMapper {
    fun mapToStudyDay(studyDayResponse: StudyDayResponse): StudyDay {
        val studyRoomUsage =
            when (studyDayResponse.studyStartTimes.size) {
                1 -> StudyRoomUsage.USED_ONCE
                2 -> StudyRoomUsage.USED_ONCE_EXTENDED_ONCE
                3 -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
                else -> StudyRoomUsage.NEVER_USED
            }
        return StudyDay(date = studyDayResponse.date, studyRoomUsage = studyRoomUsage)
    }
}
