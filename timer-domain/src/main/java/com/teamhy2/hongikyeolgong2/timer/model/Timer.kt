package com.teamhy2.hongikyeolgong2.timer.model

import java.time.Duration
import java.time.LocalTime

class Timer(
    private val startTime: LocalTime,
    private val duration: Duration,
) {
    private var endTime: LocalTime = startTime.plusSeconds(duration.seconds)
    private var remainingDuration: Duration = duration

    val formattedRemainingTime: String
        get() =
            String.format(
                "%02d:%02d:%02d",
                remainingDuration.toHours(),
                remainingDuration.toMinutes() % 60,
                remainingDuration.seconds % 60,
            )

    val remainingMinutes: Long
        get() = remainingDuration.toMinutes()

    fun tick() {
        if (remainingDuration > Duration.ZERO) {
            remainingDuration = remainingDuration.minusSeconds(1)
        }
    }

    fun isTimeOver(): Boolean {
        return remainingDuration <= Duration.ZERO
    }

    val formattedStartTime: String = startTime.toString()
    val formattedEndTime: String = endTime.toString()
}
