package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.Promotion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PromotionRepository {
    val isPromotionDismissed: Flow<Boolean>

    suspend fun fetchPromotionData(): Result<Promotion>

    suspend fun savePromotionDismissPeriod(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Unit>
}
