package com.teamhy2.feature.home.model

import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying

data class HomeUiState(
    val wiseSaying: WiseSaying = WiseSaying.DEFAULT,
    val weekStudyDays: List<WeeklyStudyDay> = WeeklyStudyDay.defaultWeek(),
)
