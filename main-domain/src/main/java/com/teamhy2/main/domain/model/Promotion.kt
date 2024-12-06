package com.teamhy2.main.domain.model

data class Promotion(
    val imageUrl: String,
    val detailUrl: String,
    val isActive: Boolean,
) {
    companion object {
        val DEFAULT =
            Promotion(
                imageUrl = "",
                detailUrl = "",
                isActive = false,
            )
    }
}
