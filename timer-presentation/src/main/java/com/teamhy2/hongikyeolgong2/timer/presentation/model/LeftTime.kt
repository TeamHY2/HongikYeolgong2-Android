package com.teamhy2.hongikyeolgong2.timer.presentation.model

import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale

@JvmInline
value class LeftTime(val value: String) {
    companion object {
        private const val LEFT_TIME_FORMAT: String = "%02d:%02d:%02d"
        private const val TIME_OVER = "00:00:00"

        fun create(endTime: LocalDateTime): LeftTime {
            val now = LocalDateTime.now()
            if (now.isAfter(endTime)) {
                return LeftTime(TIME_OVER)
            }

            val leftTime: Duration = Duration.between(now, endTime)
            val formattedLeftTime =
                String.format(
                    Locale.KOREA,
                    LEFT_TIME_FORMAT,
                    leftTime.toHours(),
                    leftTime.toMinutes() % 60,
                    leftTime.seconds % 60,
                )
            return LeftTime(formattedLeftTime)
        }
    }
}
