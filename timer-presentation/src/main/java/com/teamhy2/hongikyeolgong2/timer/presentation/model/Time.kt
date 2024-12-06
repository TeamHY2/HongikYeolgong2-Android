package com.teamhy2.hongikyeolgong2.timer.presentation.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Time(
    val meridiem: Meridiem,
    val hour: String,
    val minute: String,
) {
    val hourAndMinute: String
        get() = "$hour:$minute"

    companion object {
        private const val HIGH_NOON = 12
        private const val START_END_TIME_FORMAT: String = "hh:mm"
        private val timeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern(START_END_TIME_FORMAT)

        fun create(localDateTime: LocalDateTime): Time {
            val meridiem: Meridiem = if (localDateTime.hour >= HIGH_NOON) Meridiem.PM else Meridiem.AM
            val hourAndMinute: String = localDateTime.format(timeFormatter)
            val (hour, minute) = hourAndMinute.split(":")
            return Time(
                meridiem = meridiem,
                hour = hour,
                minute = minute,
            )
        }
    }
}
