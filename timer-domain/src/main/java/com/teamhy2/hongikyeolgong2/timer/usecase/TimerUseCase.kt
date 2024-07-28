package com.teamhy2.hongikyeolgong2.timer.usecase

import com.teamhy2.hongikyeolgong2.timer.model.Timer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimerUseCase(private val timer: Timer) {
    val formattedRemainingTime: String
        get() = timer.formattedRemainingTime

    val remainingMinutes: Long
        get() = timer.remainingMinutes

    private fun isTimeOver(): Boolean {
        return timer.isTimeOver()
    }

    val startTime: String
        get() = timer.formattedStartTime

    val endTime: String
        get() = timer.formattedEndTime

    fun timerEvents(): Flow<Long> =
        flow {
            while (!isTimeOver()) {
                timer.tick()
                emit(remainingMinutes)
                delay(1000)
            }
            emit(remainingMinutes)
        }
}
