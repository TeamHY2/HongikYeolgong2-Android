package com.teamhy2.hongikyeolgong2.timer.prsentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.Yellow100
import com.teamhy2.hongikyeolgong2.timer.presentation.R

@Composable
fun HY2Timer(
    remainingTime: String,
    startTime: String,
    endTime: String,
    modifier: Modifier = Modifier,
    onTimeRemainingChange: ((Long) -> Unit)? = null,
) {
    val remainingMinutes =
        remainingTime.split(":").let {
            it[0].toLong() * 60 + it[1].toLong()
        }

    LaunchedEffect(remainingMinutes) {
        when (remainingMinutes) {
            30L, 5L, 0L -> onTimeRemainingChange?.invoke(remainingMinutes)
        }
    }

    val leftTimeTextColor = if (remainingMinutes < 30) Yellow100 else Gray100

    Column(
        modifier =
            modifier
                .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.timer_start),
                style = HY2Theme.typography.body02,
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
                style = HY2Theme.typography.body02,
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
                text = stringResource(id = R.string.timer_am),
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
                text = stringResource(id = R.string.timer_pm),
                style = HY2Theme.typography.body03,
                color = Gray100,
                modifier =
                    Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 4.dp),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Time Left",
            style = HY2Theme.typography.body02,
            color = Gray300,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = remainingTime, style = HY2Theme.typography.body01, color = leftTimeTextColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun HY2TimerPreview() {
    HY2Theme {
        HY2Timer(
            remainingTime = "00:30:00",
            startTime = "11:30",
            endTime = "01:30",
            modifier = Modifier.background(Black),
            onTimeRemainingChange = { event ->
                when (event) {
                    30L -> println("30 minutes remaining event triggered")
                    5L -> println("5 minutes remaining event triggered")
                    0L -> println("Timer finished event triggered")
                }
            },
        )
    }
}
