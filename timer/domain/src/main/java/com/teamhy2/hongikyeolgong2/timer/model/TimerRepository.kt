package com.teamhy2.hongikyeolgong2.timer.model

interface TimerRepository {
    suspend fun getStudyRoomHourDuration(): Int

    suspend fun getCurrentTimerDuration(): TimerDuration?

    suspend fun getCurrentStudySessionId(): Long?

    suspend fun setCurrentTimerDuration(timerDuration: TimerDuration)

    suspend fun setCurrentStudySessionId(studySessionId: Long)

    suspend fun clearCurrentTimerDuration()

    suspend fun clearCurrentStudySessionId()

    companion object {
        const val MINIMUM_STUDY_ROOM_HOUR_DURATION = 4
    }
}
