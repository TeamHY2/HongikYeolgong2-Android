package com.teamhy2.feature.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2TimePicker
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.feature.home.component.InitTimerComponent
import com.teamhy2.feature.home.component.RunningTimerComponent
import com.teamhy2.feature.home.component.WeeklyStudyCalendar
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.feature.main.MainViewModel
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.prsentation.TimerViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeRoute(
    seatingChartUrl: String,
    onSendNotification: (PushText) -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val mainUiState: MainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val timerViewModel: TimerViewModel = hiltViewModel()
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val duration by timerViewModel.durationHour.collectAsStateWithLifecycle()

    mainViewModel.updateTimerStateFromTimerViewModel(timerState)

    val localShowSnackBar = LocalShowSnackBar.current
    LaunchedEffect(true) {
        mainViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    if (mainUiState.isTimePickerVisible) {
        HY2TimePicker(
            title = stringResource(R.string.main_study_room_use_start_time),
            onSelected = { selectedTime ->
                mainViewModel.run {
                    updateSelectedTime(selectedTime)
                    updateTimePickerVisibility(false)
                    updateTimerRunning(true)
                    updateTodayStudyCount()
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

    if (mainUiState.isStudyRoomExtendDialog) {
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
                    LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
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

    if (mainUiState.isStudyRoomEndDialog) {
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

    HomeScreen(
        durationAsSecond = duration.seconds,
        mainUiState = mainUiState,
        homeUiState = homeUiState,
        modifier = modifier,
        onSeatingChartClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(seatingChartUrl))
            context.startActivity(intent)
        },
        onStudyRoomStartClick = {
            mainViewModel.updateTimePickerVisibility(true)
        },
        onStudyRoomExtendClick = {
            mainViewModel.updateStudyRoomExtendDialogVisibility(true)
            startTimer(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
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
    startTime: LocalDateTime,
    mainViewModel: MainViewModel,
    timerViewModel: TimerViewModel,
    onSendNotification: (PushText) -> Unit,
) {
    timerViewModel.setTimer(
        startTime = startTime,
        events =
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
fun HomeScreen(
    durationAsSecond: Long,
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    homeUiState: HomeUiState,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(34.dp))
        HomeHeader(
            homeUiState = homeUiState,
            modifier =
                Modifier
                    .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(36.dp))
        HomeBody(
            durationAsSecond = durationAsSecond,
            mainUiState = mainUiState,
            homeUiState = homeUiState,
            onSeatingChartClick = onSeatingChartClick,
            onStudyRoomStartClick = onStudyRoomStartClick,
            onStudyRoomExtendClick = onStudyRoomExtendClick,
            onStudyRoomEndClick = onStudyRoomEndClick,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}

@Composable
private fun HomeHeader(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    WeeklyStudyCalendar(weeklyStudyDays = homeUiState.weekStudyDays, modifier = modifier)
}

@Composable
private fun HomeBody(
    durationAsSecond: Long,
    mainUiState: MainUiState,
    homeUiState: HomeUiState,
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (mainUiState.isTimerRunning) {
            true -> {
                RunningTimerComponent(
                    durationAsSecond = durationAsSecond,
                    startTime = mainUiState.startTime,
                    endTime = mainUiState.endTime,
                    startTimeMeridiem = mainUiState.startTimeMeridiem,
                    endTimeMeridiem = mainUiState.endTimeMeridiem,
                    leftTime = mainUiState.leftTime,
                    onStudyRoomExtendClick = onStudyRoomExtendClick,
                    onStudyRoomEndClick = onStudyRoomEndClick,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            false -> {
                InitTimerComponent(
                    onSeatingChartClick = onSeatingChartClick,
                    onStudyRoomStartClick = onStudyRoomStartClick,
                    uiState = homeUiState,
                    modifier = Modifier.weight(12f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val mainUiState = MainUiState()
    val homeUiState = HomeUiState()

    HY2Theme {
        HomeScreen(
            durationAsSecond = 0,
            onSeatingChartClick = { },
            onStudyRoomStartClick = { },
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            mainUiState = mainUiState,
            homeUiState = homeUiState,
        )
    }
}
