package com.teamhy2.feature.main.mapper

import com.hongikyeolgong2.calendar.model.StudyRoomUsage

object StudyDayMapper {
    private fun mapToStudyRoomUsage(studyStartTimesCount: Int): StudyRoomUsage {
        return when (studyStartTimesCount) {
            0 -> StudyRoomUsage.NEVER_USED
            1 -> StudyRoomUsage.USED_ONCE
            2 -> StudyRoomUsage.USED_ONCE_EXTENDED_ONCE
            3 -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
            else -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
        }
    }

    fun mapStudyRoomUsageToStarCount(studyRoomUsage: StudyRoomUsage): Int {
        return when (studyRoomUsage) {
            StudyRoomUsage.USED_ONCE -> 1
            StudyRoomUsage.USED_ONCE_EXTENDED_ONCE -> 2
            StudyRoomUsage.USED_ONCE_EXTENDED_TWICE -> 3
            StudyRoomUsage.NEVER_USED -> 0
        }
    }

    fun getNextStudyRoomUsage(studyRoomUsage: StudyRoomUsage): StudyRoomUsage {
        return when (studyRoomUsage) {
            StudyRoomUsage.USED_ONCE -> StudyRoomUsage.USED_ONCE_EXTENDED_ONCE
            StudyRoomUsage.USED_ONCE_EXTENDED_ONCE -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
            StudyRoomUsage.USED_ONCE_EXTENDED_TWICE -> StudyRoomUsage.USED_ONCE_EXTENDED_TWICE
            StudyRoomUsage.NEVER_USED -> StudyRoomUsage.USED_ONCE
        }
    }
}
