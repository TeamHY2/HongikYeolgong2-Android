package com.teamhy2.main.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.dto.StudyEndRequest
import com.benenfeldt.remote.dto.StudyStartRequest
import com.benenfeldt.remote.dto.StudyingUserResponse
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.main.data.mapper.toDomain
import com.teamhy2.main.domain.model.StudyEndResult
import com.teamhy2.main.domain.model.StudyStartResult
import com.teamhy2.main.domain.model.StudyingUser
import com.teamhy2.main.domain.repository.StudyRepository
import javax.inject.Inject

class RemoteStudyRepository
    @Inject
    constructor(private val studyService: StudyService) :
    StudyRepository {
        override suspend fun startStudy(startTime: String): Result<StudyStartResult> {
            return studyService.postStudyStart(StudyStartRequest(startTime)).toResult {
                it.data.toDomain()
            }
        }

        override suspend fun endStudy(
            studySessionId: Long,
            endTime: String,
        ): Result<StudyEndResult> {
            return studyService.patchStudyEnd(StudyEndRequest(studySessionId, endTime)).toResult {
                it.data.toDomain()
            }
        }

        override suspend fun getStudyingUsers(): Result<List<StudyingUser>> {
            return studyService.getStudyingUsers().toResult {
                it.data.map(StudyingUserResponse::toDomain)
            }
        }
    }
