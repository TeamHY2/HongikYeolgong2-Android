package com.teamhy2.main.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.dto.StudyDayRequest
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.main.data.mapper.toDomain
import com.teamhy2.main.domain.model.StudyDayRecord
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.repository.StudyDayRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RemoteStudyDayRepository
    @Inject
    constructor(
        private val studyService: StudyService,
    ) : StudyDayRepository {
        override suspend fun fetchWeeklyStudyDay(): Result<List<WeeklyStudyDay>> {
            return studyService.getWeeklyStudyDay().toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }

        override suspend fun saveStudyDay(
            startTime: LocalDateTime,
            endTime: LocalDateTime,
        ): Result<StudyDayRecord> {
            val studyDayRequest =
                StudyDayRequest(
                    startTime = startTime.format(studyDayFormatter),
                    endTime = endTime.format(studyDayFormatter),
                )

            return studyService.postStudyDay(studyDayRequest).toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }

        companion object {
            private val studyDayFormatter = DateTimeFormatter.ISO_DATE_TIME
        }
    }
