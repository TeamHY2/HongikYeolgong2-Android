package com.teamhy2.hongikyeolgong2.timer.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime

class Timer(
    val startTime: LocalDateTime,
    duration: Duration,
    private val events: Map<Long, () -> Unit>,
) {
    var endTime: LocalDateTime = startTime.plusSeconds(duration.seconds)
        private set

    init {
        require(events.keys.containsAll(EVENT_TIMES)) {
            "포함되지 않은 시간이 있습니다."
        }
    }

    private val leftTime: Duration
        get() = calculateLeftTime()

    fun emitTimerEvents(): Flow<Long> =
        flow {
            while (isTimeOver().not()) {
                val leftSeconds: Long = leftTime.seconds
                events[leftSeconds]?.invoke()
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
        const val TIME_OVER: Long = 0L
        private const val ONE_SECOND: Long = 1000L

        private val EVENT_TIMES: List<Long> = listOf(TIME_OVER)

        val IDLE: Timer =
            Timer(
                startTime = LocalDateTime.MAX,
                duration = Duration.ZERO,
                events = mapOf(TIME_OVER to {}),
            )
    }
}
