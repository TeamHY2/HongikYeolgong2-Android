package com.teamhy2.hongikyeolgong2.timer.presentation

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerServiceManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        fun startTimer(
            startDateTime: LocalDateTime,
            endDateTime: LocalDateTime,
        ) {
            val intent =
                Intent(context, TimerForegroundService::class.java).apply {
                    action = ACTION_START
                    putExtra(TimerForegroundService.EXTRA_START_TIME, startDateTime.toEpochMilli())
                    putExtra(TimerForegroundService.EXTRA_END_TIME, endDateTime.toEpochMilli())
                }
            context.startForegroundService(intent)
        }

        fun stopTimer() {
            val intent =
                Intent(context, TimerForegroundService::class.java).apply {
                    action = ACTION_STOP
                }
            context.startService(intent)
        }

        fun extendTimer(newEndDateTime: LocalDateTime) {
            val intent =
                Intent(context, TimerForegroundService::class.java).apply {
                    action = ACTION_EXTEND
                    putExtra(TimerForegroundService.EXTRA_NEW_END_TIME, newEndDateTime.toEpochMilli())
                }
            context.startService(intent)
        }

        private fun LocalDateTime.toEpochMilli(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        companion object {
            const val ACTION_START = "com.teamhy2.action.timer.START"
            const val ACTION_STOP = "com.teamhy2.action.timer.STOP"
            const val ACTION_EXTEND = "com.teamhy2.action.timer.EXTEND"
        }
    }
