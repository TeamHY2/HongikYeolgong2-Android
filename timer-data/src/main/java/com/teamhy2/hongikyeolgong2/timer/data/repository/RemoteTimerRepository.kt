package com.teamhy2.hongikyeolgong2.timer.data.repository

import com.benenfeldt.remote.api.LibraryService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.hongikyeolgong2.timer.model.TimerDataSource
import com.teamhy2.hongikyeolgong2.timer.model.TimerDuration
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import java.time.LocalDateTime
import javax.inject.Inject

class RemoteTimerRepository
    @Inject
    constructor(
        private val libraryService: LibraryService,
        private val timerDataSource: TimerDataSource,
    ) : TimerRepository {
        override suspend fun getStudyRoomHourDuration(): Int {
            val studyRoomHourDuration =
                libraryService.getStudyRoomHourDuration()
                    .toResult { it.data.libraryHours }
                    .getOrDefault(TimerRepository.MINIMUM_STUDY_ROOM_HOUR_DURATION)

            return studyRoomHourDuration
        }

        override suspend fun getCurrentTimerDuration(): TimerDuration? {
            val startTime: LocalDateTime = timerDataSource.getStartTime() ?: return null
            val endTime: LocalDateTime = timerDataSource.getEndTime() ?: return null

            if (endTime.isBefore(LocalDateTime.now())) {
                timerDataSource.clearTimes()
                return null
            }

            return TimerDuration(startTime, endTime)
        }

        override suspend fun getCurrentStudySessionId(): Long? {
            return timerDataSource.getCurrentStudySessionId()
        }

        override suspend fun setCurrentTimerDuration(timerDuration: TimerDuration) {
            timerDataSource.saveStartTime(timerDuration.startTime.toString())
            timerDataSource.saveEndTime(timerDuration.endTime.toString())
        }

        override suspend fun setCurrentStudySessionId(studySessionId: Long) {
            timerDataSource.saveCurrentStudySessionId(studySessionId)
        }

        override suspend fun clearCurrentTimerDuration() {
            timerDataSource.clearTimes()
        }

        override suspend fun clearCurrentStudySessionId() {
            timerDataSource.clearCurrentStudySessionId()
        }
    }
