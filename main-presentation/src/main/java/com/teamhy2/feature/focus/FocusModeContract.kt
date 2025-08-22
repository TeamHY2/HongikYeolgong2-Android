package com.teamhy2.feature.focus

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.main.domain.model.StudyingUser

sealed interface FocusModeUiState : UiState {
    data object Loading : FocusModeUiState

    data class Loaded(
        val studyingUserCount: Int,
        val studyingUsers: List<StudyingUser>,
    ) : FocusModeUiState
}

sealed interface FocusModeUiIntent : UiIntent {
    data object EnterFocusModeScreen : FocusModeUiIntent

    data object Tick : FocusModeUiIntent
}

sealed interface FocusModeSideEffect : SideEffect {
    data class ShowSnackBar(val throwable: Throwable) : FocusModeSideEffect
}
