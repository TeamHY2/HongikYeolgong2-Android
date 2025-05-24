package com.teamhy2.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2LoadingScreen
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.feature.home.component.RunningTimer
import com.teamhy2.feature.home.navigation.popUpToHome
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.presentation.TimerViewModel
import com.teamhy2.hongikyeolgong2.timer.presentation.model.LeftTime
import com.teamhy2.hongikyeolgong2.timer.presentation.model.Meridiem
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerTime
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import kotlinx.coroutines.flow.collectLatest
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun FocusModeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
) {
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val tracker = LocalTracker.current
    val localNavController = LocalNavController.current
    val localShowSnackBar = LocalShowSnackBar.current

    var isStudyRoomExtendDialog by rememberSaveable { mutableStateOf(false) }
    var isStudyRoomEndDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        timerViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    if (isStudyRoomExtendDialog) {
        HY2Dialog(
            description = stringResource(R.string.main_extend_dialog_title),
            leftButtonText = stringResource(R.string.main_extend_dialog_negative_button),
            rightButtonText = stringResource(R.string.main_extend_dialog_positive_button),
            onLeftButtonClick = { isStudyRoomExtendDialog = false },
            onRightButtonClick = {
                isStudyRoomExtendDialog = false
                homeViewModel.increaseTodayStudyCount()
                timerViewModel.extendTime()
                tracker.trackEvent("StudyExtendButton")
            },
            onDismiss = {
                isStudyRoomExtendDialog = false
            },
        )
    }

    if (isStudyRoomEndDialog) {
        HY2Dialog(
            description = stringResource(R.string.main_end_dialog_title),
            leftButtonText = stringResource(R.string.main_end_dialog_negative_button),
            rightButtonText = stringResource(R.string.main_end_dialog_positive_button),
            onLeftButtonClick = {
                isStudyRoomEndDialog = false
            },
            onRightButtonClick = {
                isStudyRoomEndDialog = false
                timerViewModel.stopTimer()
                localNavController.popUpToHome()
                tracker.trackEvent("StudyEndButton")
            },
            onDismiss = {
                isStudyRoomEndDialog = false
            },
        )
    }

    when (timerState) {
        TimerUiState.Idle -> HY2LoadingScreen()
        is TimerUiState.Running -> {
            FocusModeScreen(
                timerUiState = timerState as TimerUiState.Running,
                onStudyRoomExtendClick = {
                    isStudyRoomExtendDialog = true
                },
                onStudyRoomEndClick = {
                    isStudyRoomEndDialog = true
                },
                onCloseClick = { localNavController.popBackStack() },
                modifier = modifier,
            )
        }
    }
}

@Composable
fun FocusModeScreen(
    timerUiState: TimerUiState.Running,
    onStudyRoomEndClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        IconButton(onClick = onCloseClick) {
            Icon(
                painter = painterResource(com.teamhy2.designsystem.R.drawable.ic_close),
                contentDescription = "닫기",
                tint = Gray100,
            )
        }
        RunningTimer(
            timerUiState = timerUiState,
            onStudyRoomEndClick = onStudyRoomEndClick,
            onStudyRoomExtendClick = onStudyRoomExtendClick,
        )
    }
}

@Preview
@Composable
private fun FocusModeScreenPreview() {
    HY2Theme {
        val timerUiState =
            TimerUiState.Running(
                startDateTime = LocalDateTime.now(),
                startTime =
                    TimerTime(
                        meridiem = Meridiem.AM,
                        hour = "11",
                        minute = "30",
                    ),
                endTime =
                    TimerTime(
                        meridiem = Meridiem.PM,
                        hour = "12",
                        minute = "00",
                    ),
                leftTime = LeftTime("00:45:00"),
                duration = Duration.ofSeconds(14400L),
            )

        FocusModeScreen(
            timerUiState = timerUiState,
            onStudyRoomEndClick = {},
            onStudyRoomExtendClick = {},
            onCloseClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
