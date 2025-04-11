package com.teamhy2.feature.home

import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying

sealed interface HomeState {
    data object Loading : HomeState

    data class Success(
        val wiseSaying: WiseSaying = WiseSaying.DEFAULT,
        val weeklyStudyDays: List<WeeklyStudyDay> = WeeklyStudyDay.defaultWeek(),
        val isPromotionDialog: Boolean = false,
        val promotion: Promotion = Promotion.DEFAULT,
    ) : HomeState
}

sealed interface HomeSideEffect {
    data class ShowError(val throwable: Throwable) : HomeSideEffect
}
