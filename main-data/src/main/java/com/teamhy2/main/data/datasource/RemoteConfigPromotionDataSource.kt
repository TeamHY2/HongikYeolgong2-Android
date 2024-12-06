package com.teamhy2.main.data.datasource

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.teamhy2.main.domain.datasource.PromotionDataSource
import com.teamhy2.main.domain.model.Promotion
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RemoteConfigPromotionDataSource
    @Inject
    constructor() : PromotionDataSource {
        private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        init {
            remoteConfig.setDefaultsAsync(DEFAULTS)
        }

        override suspend fun fetchPromotionData(): Result<Promotion> {
            return try {
                remoteConfig.fetchAndActivate().await()
                val promotionDataJson: String = remoteConfig.getString("promotionPopup")
                val jsonObject: JSONObject = JSONObject(promotionDataJson)
                val promotion: Promotion =
                    Promotion(
                        imageUrl = jsonObject.getString("imageUrl"),
                        detailUrl = jsonObject.getString("detailUrl"),
                        isActive =
                            calculatePromotionActive(
                                jsonObject.getString("startDate"),
                                jsonObject.getString("endDate"),
                            ),
                    )
                Result.success(promotion)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        private fun calculatePromotionActive(
            startDate: String,
            endDate: String,
        ): Boolean {
            return try {
                val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
                val today: LocalDate = LocalDate.now()
                val start: LocalDate = LocalDate.parse(startDate, formatter)
                val end: LocalDate = LocalDate.parse(endDate, formatter)
                today.isEqual(start) || today.isEqual(end) || (
                    today.isAfter(start) &&
                        today.isBefore(
                            end,
                        )
                )
            } catch (e: Exception) {
                false
            }
        }

        companion object {
            val DEFAULTS =
                mapOf(
                    "promotionPopup" to """{"imageUrl":"","detailUrl":"","startDate":"","endDate":""}""",
                )
        }
    }
