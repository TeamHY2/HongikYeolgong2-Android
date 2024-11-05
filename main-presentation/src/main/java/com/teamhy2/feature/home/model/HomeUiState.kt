package com.teamhy2.feature.home.model

import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import java.time.LocalDateTime

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val wiseSaying: WiseSaying = WiseSaying.DEFAULT,
        val weeklyStudyDays: List<WeeklyStudyDay> = WeeklyStudyDay.defaultWeek(),
        val isTimePickerVisible: Boolean = false,
        val isStudyRoomExtendDialog: Boolean = false,
        val isStudyRoomEndDialog: Boolean = false,
        val selectedTime: LocalDateTime = LocalDateTime.now(),
        val isTimerRunning: Boolean = false,
        val timerUiModel: TimerUiModel = TimerUiModel(),
    ) : HomeUiState

    data class Error(val message: String?) : HomeUiState
}
