package com.teamhy2.feature.focus

import StudyLamp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2LoadingScreen
import com.teamhy2.designsystem.common.HY2Spacer
import com.teamhy2.designsystem.ui.theme.Blue50
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.designsystem.util.modifier.throttleClickable
import com.teamhy2.feature.home.HomeViewModel
import com.teamhy2.feature.home.navigation.popUpToHome
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.presentation.HY2Timer
import com.teamhy2.hongikyeolgong2.timer.presentation.TimerViewModel
import com.teamhy2.hongikyeolgong2.timer.presentation.model.LeftTime
import com.teamhy2.hongikyeolgong2.timer.presentation.model.Meridiem
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerTime
import com.teamhy2.hongikyeolgong2.timer.presentation.model.TimerUiState
import com.teamhy2.main.domain.model.StudyingUser
import com.teamhy2.main.domain.model.StudyingUsers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun FocusModeRoute(
    modifier: Modifier = Modifier,
    focusModeViewModel: FocusModeViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
) {
    val focusModeState: FocusModeUiState by focusModeViewModel.uiState.collectAsStateWithLifecycle()
    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val tracker = LocalTracker.current
    val localNavController = LocalNavController.current
    val localShowSnackBar = LocalShowSnackBar.current

    LaunchedEffect(Unit) {
        focusModeViewModel.sendIntent(FocusModeUiIntent.EnterFocusModeScreen)
        launch {
            focusModeViewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is FocusModeSideEffect.ShowSnackBar -> {
                        localShowSnackBar.showSnackBar(sideEffect.throwable.message)
                    }
                }
            }
        }

        launch {
            timerViewModel.errorFlow.collectLatest { throwable ->
                localShowSnackBar.showSnackBar(throwable.message)
            }
        }
    }

    FocusModeContent(
        focusModeState = focusModeState,
        timerState = timerState,
        onStudyRoomExtendClick = {
            homeViewModel.increaseTodayStudyCount()
            timerViewModel.extendTime()
            tracker.trackEvent("StudyExtendButton")
        },
        onStudyRoomEndClick = {
            timerViewModel.stopTimer()
            localNavController.popUpToHome()
            tracker.trackEvent("StudyEndButton")
        },
        onCloseClick = {
            localNavController.popUpToHome()
        },
        onTimerStopped = {
            localNavController.popUpToHome()
        },
        modifier = modifier,
    )
}

@Composable
private fun FocusModeContent(
    focusModeState: FocusModeUiState,
    timerState: TimerUiState,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    onCloseClick: () -> Unit,
    onTimerStopped: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isStudyRoomExtendDialog by rememberSaveable { mutableStateOf(false) }
    var isStudyRoomEndDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(timerState) {
        if (timerState is TimerUiState.Idle) {
            onTimerStopped()
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
                onStudyRoomExtendClick()
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
                onStudyRoomEndClick()
            },
            onDismiss = {
                isStudyRoomEndDialog = false
            },
        )
    }

    when {
        timerState is TimerUiState.Running && focusModeState is FocusModeUiState.Loaded -> {
            FocusModeTimerRunningScreen(
                focusModeState = focusModeState,
                timerUiState = timerState,
                onStudyRoomEndClick = {
                    isStudyRoomEndDialog = true
                },
                onStudyRoomExtendClick = {
                    isStudyRoomExtendDialog = true
                },
                onCloseClick = onCloseClick,
                modifier = modifier,
            )
        }

        else -> HY2LoadingScreen()
    }
}

@Composable
private fun FocusModeTimerRunningScreen(
    focusModeState: FocusModeUiState.Loaded,
    timerUiState: TimerUiState.Running,
    onStudyRoomEndClick: () -> Unit,
    onStudyRoomExtendClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        Column(modifier = modifier) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .throttleClickable(onClick = onCloseClick),
                ) {
                    Icon(
                        painter = painterResource(com.teamhy2.designsystem.R.drawable.ic_close),
                        contentDescription = "닫기",
                        tint = Gray100,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 32.dp)
                        .offset(y = (-12).dp),
            ) {
                HY2Timer(
                    durationAsSecond = timerUiState.duration.seconds,
                    leftTime = timerUiState.leftTime.value,
                    startTime = timerUiState.startTime.formattedTime,
                    startTimeMeridiem = timerUiState.startTime.meridiem.label,
                    endTime = timerUiState.endTime.formattedTime,
                    endTimeMeridiem = timerUiState.endTime.meridiem.label,
                )
                HY2Spacer(28)
                Row {
                    HY2Button(
                        text = "열람실 이용 종료",
                        onClick = onStudyRoomEndClick,
                        textColor = Gray100,
                        backgroundColor = Gray600,
                        modifier = Modifier.weight(1f),
                    )
                    HY2Spacer(12)
                    HY2Button(
                        text = "열람실 이용 연장",
                        onClick = onStudyRoomExtendClick,
                        modifier =
                            Modifier
                                .weight(1f)
                                .alpha(
                                    1f.takeIf { timerUiState.leftTime.value <= "00:30:00" }
                                        ?: 0f,
                                ),
                    )
                }
                HY2Spacer(36)
                Text("전체", style = HY2Typography().body05, color = Gray100)
                HY2Spacer(4)
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Blue50)) {
                                append("${focusModeState.studyingUsers.studyingUsersCount}명")
                            }
                            withStyle(SpanStyle(color = Gray300)) {
                                append(" 공부중")
                            }
                        },
                    style = HY2Typography().body07,
                )
                HY2Spacer(20)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    items(focusModeState.studyingUsers.values, key = { it.userId }) {
                        StudyLamp(
                            isOn = it.studyStatus,
                            username = it.userName,
                            studyTime = it.studyDuration,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors = listOf(Color(0x0C0D1100), Color(0xFF0C0D11)),
                                start = Offset.Zero,
                                end = Offset(0f, Float.POSITIVE_INFINITY),
                            ),
                    ),
        )
    }
}

@Preview
@Composable
private fun FocusModeTimerRunningScreenPreview() {
    HY2Theme {
        val focusModeState =
            FocusModeUiState.Loaded(
                studyingUsers =
                    StudyingUsers(
                        listOf(
                            StudyingUser(
                                userId = 1,
                                userName = "매우긴닉네임입니다",
                                studyDuration = "10:00:00",
                                studyStatus = true,
                            ),
                            StudyingUser(
                                userId = 2,
                                userName = "닉네임",
                                studyDuration = "11:00:00",
                                studyStatus = true,
                            ),
                            StudyingUser(
                                userId = 3,
                                userName = "매우긴123",
                                studyDuration = "09:00:00",
                                studyStatus = false,
                            ),
                            StudyingUser(
                                userId = 4,
                                userName = "닉네임입니다",
                                studyDuration = "8:00:00",
                                studyStatus = true,
                            ),
                            StudyingUser(
                                userId = 5,
                                userName = "반달",
                                studyDuration = "8:00:00",
                                studyStatus = false,
                            ),
                        ),
                    ),
            )
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

        FocusModeTimerRunningScreen(
            focusModeState = focusModeState,
            timerUiState = timerUiState,
            onStudyRoomEndClick = {},
            onStudyRoomExtendClick = {},
            onCloseClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
