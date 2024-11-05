package com.teamhy2.record.domain.repository

import com.teamhy2.record.domain.model.StudyDuration

interface StudyDurationRepository {
    suspend fun fetchStudyDuration(): Result<StudyDuration>
}
