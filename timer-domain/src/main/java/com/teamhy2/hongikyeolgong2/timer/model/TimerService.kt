package com.teamhy2.hongikyeolgong2.timer.model

import java.time.Duration
import java.time.LocalDateTime

interface TimerService {
    fun startService(
        startTime: LocalDateTime,
        duration: Duration,
    )

    fun stopService()
}
