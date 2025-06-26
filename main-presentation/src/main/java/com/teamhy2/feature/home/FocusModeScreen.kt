package com.teamhy2.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.teamhy2.designsystem.ui.theme.Yellow100
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.designsystem.util.modifier.throttleClickable
import com.teamhy2.feature.home.navigation.popUpToHome
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.presentation.HY2Timer
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

    LaunchedEffect(Unit) {
        timerViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    FocusModeScreen(
        timerUiState = timerState,
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
        modifier = modifier,
    )
}

@Composable
fun FocusModeScreen(
    timerUiState: TimerUiState,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isStudyRoomExtendDialog by rememberSaveable { mutableStateOf(false) }
    var isStudyRoomEndDialog by rememberSaveable { mutableStateOf(false) }

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

    when (timerUiState) {
        TimerUiState.Idle -> HY2LoadingScreen()
        is TimerUiState.Running -> {
            FocusModeTimerRunningScreen(
                timerUiState = timerUiState,
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
    }
}

@Composable
fun FocusModeTimerRunningScreen(
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
                            withStyle(SpanStyle(color = Yellow100)) {
                                append("10명") // TODO: 실데이터로 변경, 컬러도 변경
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
                ) {
                    items(100) {
                        StudyLamp(
                            isOn = it % 2 == 0,
                            username = "매우긴닉네임입니다",
                            studyTime = "10:00:00",
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

@Composable
fun StudyLamp(
    isOn: Boolean,
    username: String,
    studyTime: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painter =
                painterResource(
                    R.drawable.ic_lamp_on.takeIf { isOn }
                        ?: R.drawable.ic_lamp_off,
                ),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
        )
        HY2Spacer(4)
        Text(
            text = username,
            style = HY2Typography().body07,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Blue50.takeIf { isOn } ?: Gray300,
        )
        Text(
            text = studyTime,
            style = HY2Typography().body03,
            color = Blue50.takeIf { isOn } ?: Gray300,
        )
    }
}

@Preview
@Composable
private fun StudyLampOnPreview() {
    HY2Theme {
        StudyLamp(
            isOn = true,
            username = "매우긴닉네임입니다",
            studyTime = "10:00:00",
            modifier = Modifier.width(80.dp),
        )
    }
}

@Preview
@Composable
private fun StudyLampOffPreview() {
    HY2Theme {
        StudyLamp(
            isOn = false,
            username = "반달",
            studyTime = "10:00:00",
            modifier = Modifier.width(80.dp),
        )
    }
}

@Preview
@Composable
private fun FocusModeTimerRunningScreenPreview() {
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

        FocusModeTimerRunningScreen(
            timerUiState = timerUiState,
            onStudyRoomEndClick = {},
            onStudyRoomExtendClick = {},
            onCloseClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
