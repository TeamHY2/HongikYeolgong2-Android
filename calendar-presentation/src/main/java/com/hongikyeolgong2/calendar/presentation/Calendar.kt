package com.hongikyeolgong2.calendar.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_next_month
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_previous_month

@Composable
fun Hy2Calendar(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CalendarHeader(
            title = "Jan 2024",
            onPreviousMonthClick = { /*TODO*/ },
            onNextMonthClick = { /*TODO*/ },
        )
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            DayOfWeek.entries.forEach { dayOfWeek ->
                DayOfWeek(
                    name = dayOfWeek.abbreviation,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    title: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = HY2Typography().title01,
            color = Gray100,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            modifier =
                Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onPreviousMonthClick,
                ),
            tint = Gray300,
            contentDescription = stringResource(description_previous_month),
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            modifier =
                Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onNextMonthClick,
                ),
            tint = Gray300,
            contentDescription = stringResource(description_next_month),
        )
    }
}

@Composable
fun DayOfWeek(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = HY2Typography().body04,
        color = Gray300,
    )
}

@Preview
@Composable
private fun Hy2CalendarPreview() {
    HY2Theme {
        Hy2Calendar()
    }
}

@Preview
@Composable
private fun CalendarHeaderPreview() {
    HY2Theme {
        CalendarHeader(
            title = "Jan 2024",
            onPreviousMonthClick = { },
            onNextMonthClick = { },
        )
    }
}

@Preview
@Composable
private fun DayOfWeekPreview() {
    HY2Theme {
        DayOfWeek(name = "Mon")
    }
}
