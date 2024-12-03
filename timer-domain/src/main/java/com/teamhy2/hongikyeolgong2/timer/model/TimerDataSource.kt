package com.teamhy2.hongikyeolgong2.timer.model

import java.time.LocalDateTime

interface TimerDataSource {
    suspend fun getStartTime(): LocalDateTime?

    suspend fun getEndTime(): LocalDateTime?

    suspend fun saveStartTime(startTime: String)

    suspend fun saveEndTime(endTime: String)

    suspend fun clearTimes()
}
