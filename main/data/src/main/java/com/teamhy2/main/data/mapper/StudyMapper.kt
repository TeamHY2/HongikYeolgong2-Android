package com.teamhy2.main.data.mapper

import com.benenfeldt.remote.dto.StudyEndResponse
import com.benenfeldt.remote.dto.StudyStartResponse
import com.benenfeldt.remote.dto.StudyingUserResponse
import com.teamhy2.main.domain.model.StudyEndResult
import com.teamhy2.main.domain.model.StudyStartResult
import com.teamhy2.main.domain.model.StudyingUser

fun StudyStartResponse.toDomain(): StudyStartResult =
    StudyStartResult(
        studySessionId = id,
        userId = userId,
        startTime = startTime,
    )

fun StudyEndResponse.toDomain(): StudyEndResult =
    StudyEndResult(
        studySessionId = studySessionId,
        userId = userId,
        endTime = endTime,
    )

fun StudyingUserResponse.toDomain(): StudyingUser =
    StudyingUser(
        userId = userId,
        userName = userName,
        studyDuration = studyDuration,
        studyStatus = studyStatus,
    )
