package com.teamhy2.hongikyeolgong2.timer.model

import java.time.Duration
import java.time.LocalDateTime

interface TimerService {
    fun startService(
        startDateTime: LocalDateTime,
        duration: Duration,
    )

    fun stopService()
}
