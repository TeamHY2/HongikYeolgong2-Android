package com.hongikyeolgong2.calendar.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_next_month
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.string.description_previous_month
import java.time.LocalDate

private const val DAY_DEFAULT_MARGIN = 5

@Composable
fun Hy2Calendar(
    title: String,
    days: List<StudyDay>,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CalendarHeader(
            title = title,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
        )
        Spacer(modifier = Modifier.height(12.dp))
        CalendarDayOfWeeks()
        CalendarBody(days = days)
    }
}

@Composable
private fun CalendarHeader(
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
private fun CalendarDayOfWeeks() {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            DayOfWeek(
                name = dayOfWeek.abbreviation,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DayOfWeek(
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
private fun ColumnScope.CalendarBody(
    days: List<StudyDay>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(DAY_DEFAULT_MARGIN.dp),
        horizontalArrangement = Arrangement.spacedBy(DAY_DEFAULT_MARGIN.dp),
        modifier = modifier,
    ) {
        items((days.first().date.dayOfWeek.ordinal + 1) % 7) {
            Box(modifier = Modifier.weight(1f))
        }

        items(days) {
            Day(studyDay = it, modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun Hy2CalendarPreview() {
    HY2Theme {
        val calendar by remember {
            mutableStateOf(
                Calendar(
                    studyDays =
                        listOf(
                            StudyDay(
                                date = LocalDate.now().withDayOfMonth(2),
                                studyRoomUsage = StudyRoomUsage.USED_ONCE_EXTENDED_ONCE,
                            ),
                            StudyDay(
                                date = LocalDate.now().withDayOfMonth(5),
                                studyRoomUsage = StudyRoomUsage.USED_ONCE,
                            ),
                            StudyDay(
                                date = LocalDate.now().withDayOfMonth(10),
                                studyRoomUsage = StudyRoomUsage.USED_ONCE_EXTENDED_TWICE,
                            ),
                            StudyDay(
                                date = LocalDate.now().withDayOfMonth(20),
                                studyRoomUsage = StudyRoomUsage.NEVER_USED,
                            ),
                        ),
                ),
            )
        }
        var title by remember {
            mutableStateOf(calendar.now)
        }

        var month by remember {
            mutableStateOf(calendar.getMonth())
        }

        Hy2Calendar(
            title = title,
            days = month,
            onPreviousMonthClick = {
                calendar.moveToPreviousMonth()
                title = calendar.now
                month = calendar.getMonth()
            },
            onNextMonthClick = {
                calendar.moveToNextMonth()
                title = calendar.now
                month = calendar.getMonth()
            },
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
