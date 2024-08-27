package com.teamhy2.hongikyeolgong2.timer.model

interface TimerRepository {
    suspend fun fetchStudyRoomHourDuration(): Long
}
