package com.teamhy2.main.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.main.data.mapper.toDomain
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.repository.WeeklyStudyDayRepository
import javax.inject.Inject

class RemoteWeeklyStudyDayRepository
    @Inject
    constructor(
        private val studyService: StudyService,
    ) : WeeklyStudyDayRepository {
        override suspend fun fetchWeeklyStudyDay(): Result<List<WeeklyStudyDay>> {
            return studyService.getWeeklyStudyDay().toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }
    }
