package com.teamhy2.main.domain.datasource

import com.teamhy2.main.domain.model.Promotion

interface PromotionDataSource {
    suspend fun fetchPromotionData(): Result<Promotion>
}
