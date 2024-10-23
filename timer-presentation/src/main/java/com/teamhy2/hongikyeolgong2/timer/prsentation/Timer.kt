package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.Yellow100
import com.teamhy2.hongikyeolgong2.timer.presentation.R

private const val LEFT_TIME_TEXT_COLOR_CHANGE_SECONDS = 30 * 60L

@Composable
fun HY2Timer(
    durationAsSecond: Long,
    leftTime: String,
    startTime: String,
    endTime: String,
    startTimeMeridiem: String,
    endTimeMeridiem: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        StartEndSection(
            startTime = startTime,
            endTime = endTime,
            startTimeMeridiem = startTimeMeridiem,
            endTimeMeridiem = endTimeMeridiem,
        )
        Spacer(modifier = Modifier.height(32.dp))
        TimeLeftSection(
            durationAsSecond = durationAsSecond,
            leftTime = leftTime,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun StartEndSection(
    startTime: String,
    endTime: String,
    startTimeMeridiem: String,
    endTimeMeridiem: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.timer_start),
                style = HY2Theme.typography.body04,
                color = Gray300,
            )
            Spacer(modifier = Modifier.width(13.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_timer_arrow),
                contentDescription = null,
                modifier =
                    Modifier
                        .width(102.dp)
                        .height(12.dp)
                        .align(Alignment.Top),
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = stringResource(id = R.string.timer_end),
                style = HY2Theme.typography.body04,
                color = Gray300,
            )
        }
        Spacer(modifier = Modifier.height(11.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = startTime,
                style = HY2Theme.typography.body01,
                color = Gray100,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = if (startTimeMeridiem == "AM") R.string.timer_am else R.string.timer_pm),
                style = HY2Theme.typography.body03,
                color = Gray100,
                modifier =
                    Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 4.dp),
            )
            Spacer(modifier = Modifier.width(62.dp))
            Text(
                text = endTime,
                style = HY2Theme.typography.body01,
                color = Gray100,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = if (endTimeMeridiem == "AM") R.string.timer_am else R.string.timer_pm),
                style = HY2Theme.typography.body03,
                color = Gray100,
                modifier =
                    Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 4.dp),
            )
        }
    }
}

@Composable
fun TimeLeftSection(
    durationAsSecond: Long,
    leftTime: String,
    modifier: Modifier = Modifier,
) {
    val leftSeconds: Long =
        if (leftTime.isNotEmpty()) {
            val parts = leftTime.split(":")
            when (parts.size) {
                3 -> {
                    // leftTime이 "HH:MM:SS" 형식일 경우
                    parts[0].toLong() * 3600 + parts[1].toLong() * 60 + parts[2].toLong()
                }

                2 -> {
                    // leftTime이 "MM:SS" 형식일 경우
                    parts[0].toLong() * 60 + parts[1].toLong()
                }

                else -> {
                    0L
                }
            }
        } else {
            0L
        }

    val leftTimeTextColor =
        if (leftSeconds <= LEFT_TIME_TEXT_COLOR_CHANGE_SECONDS) Yellow100 else Gray100

    val currentProgress by animateFloatAsState(
        targetValue = leftSeconds / durationAsSecond.toFloat(),
        label = "남은 시간에 대한 프로그레스 바 애니메이션",
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.timer_time_left),
            style = HY2Theme.typography.body02,
            color = Gray300,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = leftTime, style = HY2Theme.typography.body01, color = leftTimeTextColor)
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = { currentProgress },
            color = if (leftSeconds <= LEFT_TIME_TEXT_COLOR_CHANGE_SECONDS) Yellow100 else Blue100,
            trackColor = Gray600,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp,
            drawStopIndicator = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HY2TimerPreview() {
    val leftTime = "00:00:30"
    val startTime = "11:30"
    val endTime = "12:00"

    HY2Theme {
        HY2Timer(
            durationAsSecond = 14400,
            leftTime = leftTime,
            startTime = startTime,
            endTime = endTime,
            startTimeMeridiem = "AM",
            endTimeMeridiem = "PM",
            modifier = Modifier.background(Black),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartEndSectionPreview() {
    val startTime = "11:30"
    val endTime = "12:00"

    HY2Theme {
        StartEndSection(
            startTime = startTime,
            endTime = endTime,
            startTimeMeridiem = "AM",
            endTimeMeridiem = "PM",
            modifier =
                Modifier
                    .background(Black)
                    .padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLeftSectionPreview() {
    val leftTime = "00:00:30"

    HY2Theme {
        TimeLeftSection(
            durationAsSecond = 14400,
            leftTime = leftTime,
            modifier =
                Modifier
                    .background(Black)
                    .padding(16.dp),
        )
    }
}
