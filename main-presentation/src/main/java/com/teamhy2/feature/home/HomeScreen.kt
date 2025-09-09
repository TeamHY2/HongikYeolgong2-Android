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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2TimePicker
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.feature.home.component.IdleTimer
import com.teamhy2.feature.home.component.PromotionDialog
import com.teamhy2.feature.home.component.RunningTimer
import com.teamhy2.feature.home.component.WeeklyStudyCalendar
import com.teamhy2.feature.home.navigation.navigateToFocusMode
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.presentation.TimerViewModel
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.model.WiseSaying
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@Composable
fun HomeRoute(
    seatingChartUrl: String,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
) {
    val homeState: HomeState by homeViewModel.collectAsState()
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val localShowSnackBar = LocalShowSnackBar.current
    val localNavController = LocalNavController.current
    val tracker = LocalTracker.current

    var backPressedTime by remember {
        mutableLongStateOf(0L)
    }

    var isTimePickerVisible by rememberSaveable { mutableStateOf(false) }
    var isStudyRoomExtendDialog by rememberSaveable { mutableStateOf(false) }
    var isStudyRoomEndDialog by rememberSaveable { mutableStateOf(false) }

    homeViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.ShowError -> {
                localShowSnackBar.showSnackBar(sideEffect.throwable.message)
            }
        }
    }

    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - backPressedTime <= 2000L) {
            (context as Activity).finish()
        } else {
            localShowSnackBar.showSnackBar("한 번 더 누르면 앱이 종료됩니다.")
        }
        backPressedTime = System.currentTimeMillis()
    }

    LaunchedEffect(Unit) {
        tracker.trackEvent("Home")
        timerViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    SetNavigationBarColor(Color.Black)

    when (homeState) {
        is HomeState.Loading -> {
            HY2CircularLoading(modifier = Modifier.fillMaxSize())
        }

        is HomeState.Success -> {
            val uiState = homeState as HomeState.Success
            if (isTimePickerVisible) {
                HY2TimePicker(
                    title = stringResource(R.string.main_study_room_use_start_time),
                    onSelected = { selectedDateTime ->
                        homeViewModel.run {
                            isTimePickerVisible = false
                            increaseTodayStudyCount()
                        }
                        timerViewModel.setTimer(
                            startDateTime = selectedDateTime,
                        )
                        localNavController.navigateToFocusMode()
                        tracker.trackEvent("StudyStartButton")
                    },
                    onCancelled = {
                        isTimePickerVisible = false
                    },
                    onDismiss = {
                        isTimePickerVisible = false
                    },
                )
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

                        tracker.trackEvent("StudyEndButton")
                    },
                    onDismiss = {
                        isStudyRoomEndDialog = false
                    },
                )
            }

            if (uiState.isPromotionDialog) {
                PromotionDialog(
                    promotionImageUrl = uiState.promotion.imageUrl,
                    onDetailClick = {
                        homeViewModel.updatePromotionDialogVisibility(false)
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(uiState.promotion.detailUrl))
                        context.startActivity(intent)
                    },
                    onCloseClick = { homeViewModel.updatePromotionDialogVisibility(false) },
                    onCloseTodayClick = {
                        homeViewModel.updatePromotionDismissPeriod(
                            startDate = LocalDate.now(),
                            endDate = LocalDate.now(),
                        )
                        homeViewModel.updatePromotionDialogVisibility(false)
                    },
                    onDismiss = { homeViewModel.updatePromotionDialogVisibility(false) },
                )
            }

            HomeScreen(
                weeklyStudyDays = uiState.weeklyStudyDays,
                wiseSaying = uiState.wiseSaying,
                timerUiState = timerState,
                onSeatingChartClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(seatingChartUrl))
                    context.startActivity(intent)
                },
                onStudyRoomStartClick = {
                    isTimePickerVisible = true
                },
                onStudyRoomExtendClick = {
                    isStudyRoomExtendDialog = true
                },
                onStudyRoomEndClick = {
                    isStudyRoomEndDialog = true
                },
                onFocusModeClick = {
                    localNavController.navigateToFocusMode()
                },
                modifier = modifier,
            )
        }
    }
}

@Composable
fun SetNavigationBarColor(color: Color) {
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
        }
    }
}

@Composable
fun HomeScreen(
    weeklyStudyDays: List<WeeklyStudyDay>,
    wiseSaying: WiseSaying,
    timerUiState: TimerUiState,
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    onFocusModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(34.dp))
        HomeHeader(weeklyStudyDays = weeklyStudyDays)
        Spacer(modifier = Modifier.height(36.dp))
        HomeBody(
            wiseSaying = wiseSaying,
            timerUiState = timerUiState,
            onSeatingChartClick = onSeatingChartClick,
            onStudyRoomStartClick = onStudyRoomStartClick,
            onStudyRoomExtendClick = onStudyRoomExtendClick,
            onStudyRoomEndClick = onStudyRoomEndClick,
            onFocusModeClick = onFocusModeClick,
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
    wiseSaying: WiseSaying,
    timerUiState: TimerUiState,
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    onFocusModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (timerUiState) {
            is TimerUiState.Running -> {
                RunningTimer(
                    timerUiState = timerUiState,
                    onStudyRoomExtendClick = onStudyRoomExtendClick,
                    onStudyRoomEndClick = onStudyRoomEndClick,
                    onFocusModeClick = onFocusModeClick,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            is TimerUiState.Idle -> {
                IdleTimer(
                    wiseSaying = wiseSaying,
                    onSeatingChartClick = onSeatingChartClick,
                    onStudyRoomStartClick = onStudyRoomStartClick,
                    modifier = Modifier.weight(1f),
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
            timerUiState = TimerUiState.Idle,
            onSeatingChartClick = { },
            onStudyRoomStartClick = { },
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            onFocusModeClick = { },
        )
    }
}
