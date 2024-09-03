package com.teamhy2.feature.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongikyeolgong2.calendar.presentation.Hy2Calendar
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2TimePicker
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.feature.main.component.InitTimerComponent
import com.teamhy2.feature.main.component.RunningTimerComponent
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.prsentation.TimerViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun MainRoute(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    onSendNotification: (PushText) -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val uiState: MainUiState = mainViewModel.mainUiState.collectAsState().value

    val timerViewModel: TimerViewModel = hiltViewModel()
    val timerState by timerViewModel.timerState.collectAsState()

    mainViewModel.updateTimerStateFromTimerViewModel(timerState)

    if (uiState.isTimePickerVisible) {
        HY2TimePicker(
            title = stringResource(R.string.main_study_room_use_start_time),
            onSelected = { selectedTime ->
                mainViewModel.run {
                    updateSelectedTime(selectedTime)
                    updateTimePickerVisibility(false)
                    updateTimerRunning(true)
                }
                startTimer(selectedTime, mainViewModel, timerViewModel, onSendNotification)
            },
            onCancelled = {
                mainViewModel.updateTimePickerVisibility(false)
            },
            onDismiss = {
                mainViewModel.updateTimePickerVisibility(false)
            },
        )
    }

    if (uiState.isStudyRoomExtendDialog) {
        HY2Dialog(
            description = stringResource(R.string.main_extend_dialog_title),
            leftButtonText = stringResource(R.string.main_extend_dialog_negative_button),
            rightButtonText = stringResource(R.string.main_extend_dialog_positive_button),
            onLeftButtonClick = {
                mainViewModel.updateStudyRoomExtendDialogVisibility(false)
            },
            onRightButtonClick = {
                mainViewModel.updateStudyRoomExtendDialogVisibility(false)
                startTimer(
                    LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
                    mainViewModel,
                    timerViewModel,
                    onSendNotification,
                )
            },
            onDismiss = {
                mainViewModel.updateStudyRoomExtendDialogVisibility(false)
            },
        )
    }

    if (uiState.isStudyRoomEndDialog) {
        HY2Dialog(
            description = stringResource(R.string.main_end_dialog_title),
            leftButtonText = stringResource(R.string.main_end_dialog_negative_button),
            rightButtonText = stringResource(R.string.main_end_dialog_positive_button),
            onLeftButtonClick = {
                mainViewModel.updateStudyRoomEndDialogVisibility(false)
            },
            onRightButtonClick = {
                mainViewModel.updateStudyRoomEndDialogVisibility(false)
                mainViewModel.updateTimerRunning(false)
                mainViewModel.addStudyDay()
            },
            onDismiss = {
                mainViewModel.updateStudyRoomEndDialogVisibility(false)
            },
        )
    }

    MainScreen(
        uiState = uiState,
        modifier = modifier,
        onSettingClick = onSettingClick,
        onSeatingChartClick = onSeatingChartClick,
        onStudyRoomStartClick = {
            mainViewModel.updateTimePickerVisibility(true)
        },
        onPreviousMonthClick = { mainViewModel.updateCalendarMonth(false) },
        onNextMonthClick = { mainViewModel.updateCalendarMonth(true) },
        onStudyRoomExtendClick = {
            mainViewModel.updateStudyRoomExtendDialogVisibility(true)
            startTimer(
                LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
                mainViewModel,
                timerViewModel,
                onSendNotification,
            )
        },
        onStudyRoomEndClick = {
            mainViewModel.updateStudyRoomEndDialogVisibility(true)
        },
    )
}

private fun startTimer(
    startTime: LocalTime,
    mainViewModel: MainViewModel,
    timerViewModel: TimerViewModel,
    onSendNotification: (PushText) -> Unit,
) {
    timerViewModel.setTimer(
        startTime,
        Duration.ofMinutes(31),
        mapOf(
            Timer.THIRTY_MINUTES_SECONDS to {
                onSendNotification(PushText.THIRTY_MINUTES)
            },
            Timer.TEN_MINUTES_SECONDS to {
                onSendNotification(PushText.TEN_MINUTES)
            },
            Timer.TIME_OVER_SECONDS to {
                mainViewModel.updateTimerRunning(false)
                mainViewModel.addStudyDay()
            },
        ),
    )
}

@Composable
fun MainScreen(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: MainUiState,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black)
                .padding(horizontal = 24.dp),
    ) {
        MainHeader(
            onSettingClick = onSettingClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(vertical = 14.dp),
        )
        MainBody(
            onSeatingChartClick = onSeatingChartClick,
            onStudyRoomStartClick = onStudyRoomStartClick,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onStudyRoomExtendClick = onStudyRoomExtendClick,
            onStudyRoomEndClick = onStudyRoomEndClick,
            uiState = uiState,
        )
    }
}

@Composable
private fun MainHeader(
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_main_logo),
            contentDescription = stringResource(R.string.main_logo_description),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onSettingClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_main_hamburger),
                contentDescription = stringResource(R.string.main_setting_button_description),
                tint = Gray100,
            )
        }
    }
}

@Composable
private fun MainBody(
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    uiState: MainUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState.isTimerRunning) {
            true -> {
                RunningTimerComponent(
                    startTime = uiState.startTime,
                    endTime = uiState.endTime,
                    startTimeMeridiem = uiState.startTimeMeridiem,
                    endTimeMeridiem = uiState.endTimeMeridiem,
                    leftTime = uiState.leftTime,
                    onStudyRoomExtendClick = onStudyRoomExtendClick,
                    onStudyRoomEndClick = onStudyRoomEndClick,
                    modifier = Modifier.height(308.dp),
                )
            }

            false -> {
                InitTimerComponent(
                    onSeatingChartClick = onSeatingChartClick,
                    onStudyRoomStartClick = onStudyRoomStartClick,
                    uiState = uiState,
                    modifier = Modifier.height(308.dp),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Hy2Calendar(
            title = uiState.calendar.now,
            days = uiState.calendar.getMonth(),
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val state = MainUiState()

    HY2Theme {
        MainScreen(
            onSettingClick = { },
            onSeatingChartClick = { },
            onStudyRoomStartClick = { },
            onPreviousMonthClick = { },
            onNextMonthClick = { },
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            uiState = state,
        )
    }
}
