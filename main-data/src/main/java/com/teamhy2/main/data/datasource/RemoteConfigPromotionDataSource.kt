package com.teamhy2.main.data.datasource

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.teamhy2.main.data.dto.PromotionDto
import com.teamhy2.main.data.mapper.toDomain
import com.teamhy2.main.domain.datasource.PromotionDataSource
import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.util.DateUtil
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RemoteConfigPromotionDataSource @Inject constructor() : PromotionDataSource {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        remoteConfig.setDefaultsAsync(DEFAULTS)
    }

    override suspend fun fetchPromotionData(): Result<Promotion> {
        return runCatching {
            remoteConfig.fetchAndActivate().await()
            val promotionDataJson: String = remoteConfig.getString(PROMOTION_POPUP_KEY)
            val promotionDto: PromotionDto = Json.decodeFromString(promotionDataJson)
            promotionDto.copy(
                isActive =
                    DateUtil.isTodayWithinDateRange(
                        startDateString = promotionDto.startDate,
                        endDateString = promotionDto.endDate,
                    ),
            ).toDomain()
        }
    }

    companion object {
        const val PROMOTION_POPUP_KEY = "promotionPopup"
        const val DEFAULT_PROMOTION_JSON =
            """{"imageUrl":"","detailUrl":"","startDate":"","endDate":""}"""

        val DEFAULTS =
            mapOf(
                PROMOTION_POPUP_KEY to DEFAULT_PROMOTION_JSON,
            )
    }
}
