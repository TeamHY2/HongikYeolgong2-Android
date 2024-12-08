package com.teamhy2.feature.home.model

import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val wiseSaying: WiseSaying = WiseSaying.DEFAULT,
        val weeklyStudyDays: List<WeeklyStudyDay> = WeeklyStudyDay.defaultWeek(),
        val isPromotionDialog: Boolean = false,
        val promotion: Promotion = Promotion.DEFAULT,
    ) : HomeUiState

    data class Error(val message: String?) : HomeUiState
}
