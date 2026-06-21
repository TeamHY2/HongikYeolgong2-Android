package com.teamhy2.record.domain.repository

import com.teamhy2.record.domain.model.StudyDuration
import java.time.LocalDate

interface StudyDurationRepository {
    suspend fun fetchStudyDuration(date: LocalDate? = null): Result<StudyDuration>
}
