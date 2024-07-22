package com.hongikyeolgong2.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_next_month
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_previous_month

@Composable
fun Hy2Calendar(
    calendar: Calendar,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CalendarHeader(
            title = calendar.now,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
        )
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            DayOfWeek.entries.forEach { dayOfWeek ->
                DayOfWeek(
                    name = dayOfWeek.abbreviation,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            items(calendar.getMonth()) {
                Day(studyDay = it, modifier = Modifier.weight(1f))
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

@Composable
fun Day(
    studyDay: StudyDay,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(33.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gray800),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = studyDay.date.dayOfMonth.toString(),
            style = HY2Typography().body03,
            color = White,
        )
    }
}

@Preview
@Composable
private fun Hy2CalendarPreview() {
    HY2Theme {
        Hy2Calendar(
            calendar = Calendar(),
            onPreviousMonthClick = {},
            onNextMonthClick = {},
        )
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
