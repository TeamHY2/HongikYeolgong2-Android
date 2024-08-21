package com.teamhy2.feature.main.model

import com.teamhy2.main.model.WiseSaying

data class MainUiState(
    val isTimerRunning: Boolean = false,
    val wiseSaying: WiseSaying = WiseSaying("", ""),
)
