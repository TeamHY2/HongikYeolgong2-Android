package com.teamhy2.feature.main.component

import StarsComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@Composable
fun RunningTimerComponent(
    startTime: String,
    endTime: String,
    startTimeMeridiem: String,
    endTimeMeridiem: String,
    leftTime: String,
    starCount: Int,
    onStudyRoomExtendClick: () -> Unit,
    onStudyRoomEndClick: () -> Unit,
    extendThreshold: String = "00:30:00",
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box {
            HY2Timer(
                leftTime = leftTime,
                startTime = startTime,
                endTime = endTime,
                startTimeMeridiem = startTimeMeridiem,
                endTimeMeridiem = endTimeMeridiem,
            )
            StarsComponent(
                starCount = starCount,
                modifier = Modifier.align(Alignment.BottomEnd),
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        if (leftTime <= extendThreshold) {
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
    HY2Theme {
        RunningTimerComponent(
            startTime = "11:30",
            endTime = "12:00",
            startTimeMeridiem = "AM",
            endTimeMeridiem = "PM",
            leftTime = "00:14:03",
            starCount = 0,
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            modifier = Modifier.background(Black),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview_MoreThanExtendThreshold() {
    HY2Theme {
        RunningTimerComponent(
            startTime = "11:30",
            endTime = "12:00",
            startTimeMeridiem = "AM",
            endTimeMeridiem = "PM",
            leftTime = "00:45:00",
            starCount = 0,
            onStudyRoomExtendClick = { },
            onStudyRoomEndClick = { },
            modifier = Modifier.background(Black),
        )
    }
}
