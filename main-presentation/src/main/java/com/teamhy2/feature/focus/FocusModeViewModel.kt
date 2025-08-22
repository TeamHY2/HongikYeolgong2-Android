package com.teamhy2.feature.focus

import androidx.lifecycle.viewModelScope
import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.feature.focus.util.formatSecondsToTime
import com.teamhy2.feature.focus.util.parseTimeToSeconds
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
                onSuccess = {
                    startTimer()
                    FocusModeUiState.Loaded(
                        studyingUserCount = it.size,
                        studyingUsers = it,
                    )
                },
                onFailure = {
                    postSideEffect(FocusModeSideEffect.ShowSnackBar(it))
                    current
                },
            )
        }

        private fun tick(current: FocusModeUiState): FocusModeUiState {
            if (current !is FocusModeUiState.Loaded) return current

            val updatedUsers =
                current.studyingUsers.map { user ->
                    val totalSeconds = parseTimeToSeconds(user.studyDuration) + 1
                    val newDuration = formatSecondsToTime(totalSeconds)
                    user.copy(studyDuration = newDuration)
                }

            return current.copy(studyingUsers = updatedUsers)
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
            timerJob?.cancel()
            timerJob = null
        }
    }
