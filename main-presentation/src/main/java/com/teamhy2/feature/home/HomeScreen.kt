package com.teamhy2.feature.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.feature.home.component.InitTimerComponent
import com.teamhy2.feature.home.component.RunningTimerComponent
import com.teamhy2.feature.home.component.WeeklyStudyCalendar
import com.teamhy2.feature.home.model.HomeUiState
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.model.Timer
import com.teamhy2.hongikyeolgong2.timer.presentation.TimerViewModel
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiModel
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeRoute(
    seatingChartUrl: String,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val timerViewModel: TimerViewModel = hiltViewModel()
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val duration by timerViewModel.durationHour.collectAsStateWithLifecycle()
    val localShowSnackBar = LocalShowSnackBar.current
    val tracker = LocalTracker.current

    homeViewModel.updateTimerStateFromTimerViewModel(timerState)

    var backPressedTime by remember {
        mutableLongStateOf(0L)
    }

    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - backPressedTime <= 2000L) {
            (context as Activity).finish()
        } else {
            localShowSnackBar.showSnackBar("한 번 더 누르면 앱이 종료됩니다.")
        }
        backPressedTime = System.currentTimeMillis()
    }

    LaunchedEffect(true) {
        tracker.trackEvent("Home")
        homeViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    SetNavigationBarColor(Color.Black)

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
                        val updatedSelectedTime =
                            selectedTime.plusSeconds(LocalDateTime.now().second.toLong())

                        homeViewModel.run {
                            updateSelectedTime(updatedSelectedTime)
                            updateTimePickerVisibility(false)
                            updateTimerRunning(true)
                        }
                        startTimer(updatedSelectedTime, homeViewModel, timerViewModel)
                        homeViewModel.startTimerService(
                            startTime = updatedSelectedTime,
                            duration = timerViewModel.durationHour.value,
                        )
                        tracker.trackEvent("StudyStartButton")
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
                        homeViewModel.run {
                            updateStudyRoomExtendDialogVisibility(false)
                            saveStudyDay(true)
                            updateTimerRunning(true)
                            updateSelectedTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                        }
                        startTimer(
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                            homeViewModel,
                            timerViewModel,
                        )
                        homeViewModel.stopTimerService()
                        homeViewModel.startTimerService(
                            startTime = LocalDateTime.now(),
                            duration = timerViewModel.durationHour.value,
                        )
                        tracker.trackEvent("StudyExtendButton")
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
                        homeViewModel.saveStudyDay(false)
                        homeViewModel.stopTimerService()
                        tracker.trackEvent("StudyEndButton")
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

@Composable
fun SetNavigationBarColor(color: Color) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
        }
    }
}

private fun startTimer(
    startTime: LocalDateTime,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
) {
    timerViewModel.setTimer(
        startTime = startTime,
        events =
            mapOf(
                Timer.TIME_OVER to {
                    homeViewModel.updateTimerRunning(false)
                    homeViewModel.saveStudyDay(false)
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
