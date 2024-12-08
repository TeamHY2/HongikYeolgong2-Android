package com.teamhy2.main.data.mapper

import com.teamhy2.main.data.dto.PromotionDto
import com.teamhy2.main.domain.model.Promotion

fun PromotionDto.toDomain(): Promotion {
    return Promotion(
        imageUrl = this.imageUrl,
        detailUrl = this.detailUrl,
        isActive = this.isActive,
    )
}
