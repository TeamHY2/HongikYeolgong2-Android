package com.teamhy2.hongikyeolgong2.timer.model

enum class NotificationTimeFlag(val millis: Long) {
    FINISH_TIME(0L),
    TEN_MINUTES_MS(600_000L),
    THIRTY_MINUTES_MS(1_800_000L),
}
