package com.teamhy2.feature.focus

import androidx.lifecycle.viewModelScope
import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.main.domain.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel
    @Inject
    constructor(
        private val studyRepository: StudyRepository,
    ) : MviViewModel<FocusModeUiIntent, FocusModeUiState, FocusModeSideEffect>(FocusModeUiState.Loading) {
        private var timerJob: Job? = null
        private var studyingUsersRefreshCounter = 0

        override suspend fun reduceState(
            current: FocusModeUiState,
            intent: FocusModeUiIntent,
        ): FocusModeUiState {
            return when (intent) {
                FocusModeUiIntent.EnterFocusModeScreen -> getStudyingUsers(current)
                FocusModeUiIntent.Tick -> tick(current)
            }
        }

        private suspend fun getStudyingUsers(current: FocusModeUiState): FocusModeUiState {
            stopTimer()
            return studyRepository.getStudyingUsers().fold(
                onSuccess = { studyingUsers ->
                    startTimer()
                    FocusModeUiState.Loaded(studyingUsers = studyingUsers)
                },
                onFailure = {
                    postSideEffect(FocusModeSideEffect.ShowSnackBar(it))
                    current
                },
            )
        }

        private fun tick(current: FocusModeUiState): FocusModeUiState {
            if (++studyingUsersRefreshCounter % TEN_SECONDS == 0) {
                sendIntent(FocusModeUiIntent.EnterFocusModeScreen)
            }

            return runOn<FocusModeUiState.Loaded>(current) {
                copy(studyingUsers = studyingUsers.updateStudyDurationsByOneSecond())
            }
        }

        private fun startTimer() {
            timerJob =
                viewModelScope.launch {
                    while (true) {
                        delay(1000)
                        sendIntent(FocusModeUiIntent.Tick)
                    }
                }
        }

        private fun stopTimer() {
            studyingUsersRefreshCounter = 0
            timerJob?.cancel()
            timerJob = null
        }

        companion object {
            private const val TEN_SECONDS = 10
        }
    }
