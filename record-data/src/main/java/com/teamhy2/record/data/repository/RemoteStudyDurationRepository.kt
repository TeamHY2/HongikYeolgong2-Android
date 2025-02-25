package com.teamhy2.record.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.record.data.mapper.toDomain
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.domain.repository.StudyDurationRepository
import java.time.LocalDate
import javax.inject.Inject

class RemoteStudyDurationRepository
    @Inject
    constructor(
        private val studyService: StudyService,
    ) : StudyDurationRepository {
        override suspend fun fetchStudyDuration(date: LocalDate?): Result<StudyDuration> {
            return studyService.getStudyDuration(date?.toString()).toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }
    }
