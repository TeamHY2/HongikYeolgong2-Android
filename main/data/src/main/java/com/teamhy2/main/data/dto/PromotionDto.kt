package com.teamhy2.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PromotionDto(
    val imageUrl: String,
    val detailUrl: String,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean = false,
)
