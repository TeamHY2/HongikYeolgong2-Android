package com.teamhy2.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.hongikyeolgong2.timer.prsentation.HY2Timer
import com.teamhy2.hongikyeolgong2.timer.prsentation.model.TimerUiModel

@Composable
fun RunningTimerComponent(
    timerState: TimerUiModel,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    extendThreshold: String = "00:30:00",
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        HY2Timer(
            leftTime = timerState.leftTime,
            startTime = timerState.startTime,
            endTime = timerState.endTime,
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (timerState.leftTime <= extendThreshold) {
            HY2Button(
                text = stringResource(R.string.main_extend_study_room),
                onClick = onStudyRoomExtendClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        HY2Button(
            text = stringResource(R.string.main_end_study_room),
            backgroundColor = Gray600,
            textColor = Gray100,
            onClick = onStudyRoomEndClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview_LessThanExtendThreshold() {
    val dummyTimerState =
        TimerUiModel(
            startTime = "11:30",
            endTime = "12:00",
            leftTime = "00:14:03",
        )

    HY2Theme {
        RunningTimerComponent(
            timerState = dummyTimerState,
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            modifier = Modifier.background(Black),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview_MoreThanExtendThreshold() {
    val dummyTimerState =
        TimerUiModel(
            startTime = "11:30",
            endTime = "12:00",
            leftTime = "00:45:00",
        )

    HY2Theme {
        RunningTimerComponent(
            timerState = dummyTimerState,
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            modifier = Modifier.background(Black),
        )
    }
}
