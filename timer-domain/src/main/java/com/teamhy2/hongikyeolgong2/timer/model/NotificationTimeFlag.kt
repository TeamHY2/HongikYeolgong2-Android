package com.teamhy2.hongikyeolgong2.timer.model

enum class NotificationTimeFlag(val millis: Long) {
    FINISH_TIME(0L),
    TEN_MINUTES(600_000L),
    THIRTY_MINUTES(1_800_000L),
}
