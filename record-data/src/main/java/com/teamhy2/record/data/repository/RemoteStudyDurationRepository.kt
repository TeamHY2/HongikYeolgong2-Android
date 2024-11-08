package com.teamhy2.record.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.record.data.mapper.toDomain
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.domain.repository.StudyDurationRepository
import javax.inject.Inject

class RemoteStudyDurationRepository
    @Inject
    constructor(
        private val studyService: StudyService,
    ) : StudyDurationRepository {
        override suspend fun fetchStudyDuration(): Result<StudyDuration> {
            return studyService.getStudyDuration().toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }
    }
