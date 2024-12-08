package com.teamhy2.hongikyeolgong2.timer.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime

class Timer(
    val startTime: LocalDateTime,
    duration: Duration,
) {
    var endTime: LocalDateTime = startTime.plusSeconds(duration.seconds)
        private set

    private val leftTime: Duration
        get() = calculateLeftTime()

    fun emitTimerEvents(): Flow<Long> =
        flow {
            while (isTimeOver().not()) {
                val leftSeconds: Long = leftTime.seconds
                emit(leftSeconds)
                delay(ONE_SECOND)
            }
        }

    private fun isTimeOver(): Boolean {
        return leftTime <= Duration.ZERO
    }

    private fun calculateLeftTime(): Duration {
        val now: LocalDateTime = LocalDateTime.now()
        if (now.isAfter(endTime)) {
            return Duration.ZERO
        }
        return Duration.between(now, endTime)
    }

    companion object {
        private const val ONE_SECOND: Long = 1000L

        val IDLE: Timer =
            Timer(
                startTime = LocalDateTime.MAX,
                duration = Duration.ZERO,
            )
    }
}
