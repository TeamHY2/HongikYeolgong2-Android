package com.teamhy2.hongikyeolgong2.timer.model

interface TimerRepository {
    suspend fun getStudyRoomHourDuration(): Int

    suspend fun getCurrentTimerDuration(): TimerDuration?

    suspend fun setCurrentTimerDuration(timerDuration: TimerDuration)

    suspend fun clearCurrentTimerDuration()

    companion object {
        const val MINIMUM_STUDY_ROOM_HOUR_DURATION = 4
    }
}
