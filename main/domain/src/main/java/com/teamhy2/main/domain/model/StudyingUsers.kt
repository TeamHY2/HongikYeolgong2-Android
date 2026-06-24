package com.teamhy2.main.domain.model

import com.teamhy2.main.domain.util.formatSecondsToTime
import com.teamhy2.main.domain.util.parseTimeToSeconds

data class StudyingUsers(val values: List<StudyingUser>) {
    val studyingUsersCount: Int = values.count { it.studyStatus }

    fun updateStudyDurationsByOneSecond(): StudyingUsers {
        if (studyingUsersCount == 0) return this

        val updated: List<StudyingUser> =
            values
                .map { user ->
                    if (user.studyStatus) {
                        val totalSeconds: Long = parseTimeToSeconds(user.studyDuration) + ONE_SECOND
                        val newDuration: String = formatSecondsToTime(totalSeconds)
                        user.copy(studyDuration = newDuration)
                    } else {
                        user
                    }
                }

        return StudyingUsers(updated)
    }

    companion object {
        private const val ONE_SECOND = 1
    }
}
