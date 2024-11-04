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
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2TimePicker
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.feature.home.component.InitTimerComponent
import com.teamhy2.feature.home.component.RunningTimerComponent
import com.teamhy2.feature.home.component.WeeklyStudyCalendar
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.prsentation.TimerViewModel
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeRoute(
    seatingChartUrl: String,
    onSendNotification: (PushText) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val timerViewModel: TimerViewModel = hiltViewModel()
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val duration by timerViewModel.durationHour.collectAsStateWithLifecycle()

    homeViewModel.updateTimerStateFromTimerViewModel(timerState)

    val localShowSnackBar = LocalShowSnackBar.current
    LaunchedEffect(true) {
        homeViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    when (homeUiState) {
        is HomeUiState.Loading -> {
            HY2CircularLoading(modifier = Modifier.fillMaxSize())
        }

        is HomeUiState.Success -> {
            val uiState = homeUiState as HomeUiState.Success

            if (uiState.isTimePickerVisible) {
                HY2TimePicker(
                    title = stringResource(R.string.main_study_room_use_start_time),
                    onSelected = { selectedTime ->
                        homeViewModel.run {
                            updateSelectedTime(selectedTime)
                            updateTimePickerVisibility(false)
                            updateTimerRunning(true)
                        }
                        startTimer(selectedTime, homeViewModel, timerViewModel, onSendNotification)
                    },
                    onCancelled = {
                        homeViewModel.updateTimePickerVisibility(false)
                    },
                    onDismiss = {
                        homeViewModel.updateTimePickerVisibility(false)
                    },
                )
            }

            if (uiState.isStudyRoomExtendDialog) {
                HY2Dialog(
                    description = stringResource(R.string.main_extend_dialog_title),
                    leftButtonText = stringResource(R.string.main_extend_dialog_negative_button),
                    rightButtonText = stringResource(R.string.main_extend_dialog_positive_button),
                    onLeftButtonClick = {
                        homeViewModel.updateStudyRoomExtendDialogVisibility(false)
                    },
                    onRightButtonClick = {
                        homeViewModel.updateStudyRoomExtendDialogVisibility(false)
                        startTimer(
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                            homeViewModel,
                            timerViewModel,
                            onSendNotification,
                        )
                    },
                    onDismiss = {
                        homeViewModel.updateStudyRoomExtendDialogVisibility(false)
                    },
                )
            }

            if (uiState.isStudyRoomEndDialog) {
                HY2Dialog(
                    description = stringResource(R.string.main_end_dialog_title),
                    leftButtonText = stringResource(R.string.main_end_dialog_negative_button),
                    rightButtonText = stringResource(R.string.main_end_dialog_positive_button),
                    onLeftButtonClick = {
                        homeViewModel.updateStudyRoomEndDialogVisibility(false)
                    },
                    onRightButtonClick = {
                        homeViewModel.updateStudyRoomEndDialogVisibility(false)
                        homeViewModel.updateTimerRunning(false)
                        // homeViewModel.addStudyDay()
                    },
                    onDismiss = {
                        homeViewModel.updateStudyRoomEndDialogVisibility(false)
                    },
                )
            }

            HomeScreen(
                weeklyStudyDays = uiState.weeklyStudyDays,
                wiseSaying = uiState.wiseSaying,
                durationAsSecond = duration.seconds,
                isTimerRunning = uiState.isTimerRunning,
                timerUiModel = uiState.timerUiModel,
                modifier = modifier,
                onSeatingChartClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(seatingChartUrl))
                    context.startActivity(intent)
                },
                onStudyRoomStartClick = {
                    homeViewModel.updateTimePickerVisibility(true)
                },
                onStudyRoomExtendClick = {
                    homeViewModel.updateStudyRoomExtendDialogVisibility(true)
                    startTimer(
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                        homeViewModel,
                        timerViewModel,
                        onSendNotification,
                    )
                },
                onStudyRoomEndClick = {
                    homeViewModel.updateStudyRoomEndDialogVisibility(true)
                },
            )
        }

        is HomeUiState.Error -> {
            localShowSnackBar.showSnackBar(
                (homeUiState as HomeUiState.Error).message,
            )
        }
    }
}

private fun startTimer(
    startTime: LocalDateTime,
    homeViewModel: HomeViewModel,
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
                    homeViewModel.updateTimerRunning(false)
                    // homeViewModel.addStudyDay()
                },
            ),
    )
}

@Composable
fun HomeScreen(
    weeklyStudyDays: List<WeeklyStudyDay>,
    wiseSaying: WiseSaying,
    durationAsSecond: Long,
    isTimerRunning: Boolean,
    timerUiModel: TimerUiModel,
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
    ) {
        Spacer(modifier = Modifier.height(34.dp))
        HomeHeader(
            weeklyStudyDays = weeklyStudyDays,
            modifier =
            Modifier,
        )
        Spacer(modifier = Modifier.height(36.dp))
        HomeBody(
            durationAsSecond = durationAsSecond,
            wiseSaying = wiseSaying,
            isTimerRunning = isTimerRunning,
            timerUiModel = timerUiModel,
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
    weeklyStudyDays: List<WeeklyStudyDay>,
    modifier: Modifier = Modifier,
) {
    WeeklyStudyCalendar(
        weeklyStudyDays = weeklyStudyDays,
        modifier =
            modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
    )
}

@Composable
private fun HomeBody(
    durationAsSecond: Long,
    wiseSaying: WiseSaying,
    isTimerRunning: Boolean,
    timerUiModel: TimerUiModel,
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
        when (isTimerRunning) {
            true -> {
                RunningTimerComponent(
                    durationAsSecond = durationAsSecond,
                    startTime = timerUiModel.startTime,
                    endTime = timerUiModel.endTime,
                    startTimeMeridiem = timerUiModel.startTimeMeridiem,
                    endTimeMeridiem = timerUiModel.endTimeMeridiem,
                    leftTime = timerUiModel.leftTime,
                    onStudyRoomExtendClick = onStudyRoomExtendClick,
                    onStudyRoomEndClick = onStudyRoomEndClick,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            false -> {
                InitTimerComponent(
                    wiseSaying = wiseSaying,
                    onSeatingChartClick = onSeatingChartClick,
                    onStudyRoomStartClick = onStudyRoomStartClick,
                    modifier = Modifier.weight(12f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HY2Theme {
        HomeScreen(
            weeklyStudyDays = WeeklyStudyDay.defaultWeek(),
            wiseSaying = WiseSaying.DEFAULT,
            durationAsSecond = 0,
            onSeatingChartClick = { },
            onStudyRoomStartClick = { },
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            isTimerRunning = false,
            timerUiModel = TimerUiModel(),
        )
    }
}
