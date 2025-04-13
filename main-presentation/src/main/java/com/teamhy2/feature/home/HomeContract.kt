package com.teamhy2.feature.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
sealed interface HomeState {
    @Immutable
    data object Loading : HomeState

    @Immutable
    data class Success(
        val wiseSaying: WiseSaying = WiseSaying.DEFAULT,
        val weeklyStudyDays: ImmutableList<WeeklyStudyDay> =
            WeeklyStudyDay
                .defaultWeek()
                .toImmutableList(),
        val isPromotionDialog: Boolean = false,
        val promotion: Promotion = Promotion.DEFAULT,
    ) : HomeState
}

sealed interface HomeSideEffect {
    data class ShowError(val throwable: Throwable) : HomeSideEffect
}
