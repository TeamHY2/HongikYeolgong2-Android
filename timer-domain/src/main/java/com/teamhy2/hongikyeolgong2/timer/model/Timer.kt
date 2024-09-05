package com.teamhy2.hongikyeolgong2.timer.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Timer(
    private val startTime: LocalTime,
    private val duration: Duration,
    private val events: Map<Long, () -> Unit>,
) {
    var endTime: LocalTime = startTime.plusSeconds(duration.seconds)
        private set
    var leftTime: Duration = duration
        private set

    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(START_END_TIME_FORMAT)

    init {
        require(events.keys.containsAll(EVENT_TIMES)) {
            "포함되지 않은 시간이 있습니다."
        }
    }

    val formattedLeftTime: String
        get() =
            String.format(
                LEFT_TIME_FORMAT,
                leftTime.toHours(),
                leftTime.toMinutes() % 60,
                leftTime.seconds % 60,
            )

    val formattedStartTime: String
        get() = startTime.format(timeFormatter)

    val formattedEndTime: String
        get() = endTime.format(timeFormatter)

    val formattedStartTimeMeridiem: String
        get() = if (startTime.hour >= 12) "PM" else "AM"

    val formattedEndTimeMeridiem: String
        get() = if (endTime.hour >= 12) "PM" else "AM"

    private val leftSeconds: Long
        get() = leftTime.seconds

    fun emitTimerEvents(): Flow<Long> =
        flow {
            while (!isTimeOver()) {
                tick()
                events[leftSeconds]?.invoke()
                emit(leftSeconds)
                delay(DELAY_MILLIS)
            }
        }

    private fun isTimeOver(): Boolean {
        return leftTime <= Duration.ZERO
    }

    private fun tick() {
        if (leftTime > Duration.ZERO) {
            leftTime = leftTime.minusSeconds(1)
        }
    }

    companion object {
        private const val START_END_TIME_FORMAT: String = "hh:mm"
        private const val LEFT_TIME_FORMAT: String = "%02d:%02d:%02d"
        const val THIRTY_MINUTES_SECONDS: Long = 30 * 60L
        const val TEN_MINUTES_SECONDS: Long = 10 * 60L
        const val TIME_OVER_SECONDS: Long = 0L
        private const val DELAY_MILLIS: Long = 1000L

        private val EVENT_TIMES: List<Long> =
            listOf(THIRTY_MINUTES_SECONDS, TEN_MINUTES_SECONDS, TIME_OVER_SECONDS)
    }
}
