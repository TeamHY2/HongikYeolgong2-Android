package com.teamhy2.hongikyeolgong2.timer.model

import java.time.LocalDateTime

interface TimerService {
    fun startService(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    )

    fun stopService()
}
