package com.teamhy2.hongikyeolgong2.timer.data.repository

import com.benenfeldt.remote.api.LibraryService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import javax.inject.Inject

class RemoteTimerRepository
    @Inject
    constructor(
        private val libraryService: LibraryService,
    ) : TimerRepository {
        override suspend fun getStudyRoomHourDuration(): Result<Int> {
            return libraryService.getStudyRoomHourDuration().toResult { it.data.libraryHours }
        }
    }
