package com.teamhy2.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.main.domain.model.WeeklyStudyDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor

private val DAYS_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")
private const val DATE_FORMAT_PATTERN = "M/d"

@Composable
fun WeeklyStudyCalendar(
    weeklyStudyDays: List<WeeklyStudyDay>,
    modifier: Modifier = Modifier,
) {
    val currentDate = LocalDate.now()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        DAYS_OF_WEEK.forEachIndexed { index, dayOfWeek ->
            val isFutureDate = isDateInFuture(weeklyStudyDays[index].date, currentDate)

            WeeklyDayComponent(
                dayOfWeek = dayOfWeek,
                studyCount = weeklyStudyDays[index].studyCount,
                date = weeklyStudyDays[index].date,
                textColor = if (isFutureDate) Gray400 else White,
            )
        }
    }
}

private fun isDateInFuture(
    dateText: String,
    currentDate: LocalDate,
): Boolean {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
    val parsedMonthDay: TemporalAccessor = formatter.parse(dateText)
    val month: Int = parsedMonthDay[ChronoField.MONTH_OF_YEAR]
    val day: Int = parsedMonthDay[ChronoField.DAY_OF_MONTH]
    val parsedDate: LocalDate = LocalDate.of(currentDate.year, month, day)

    return parsedDate.isAfter(currentDate)
}

@Preview(showBackground = true)
@Composable
private fun PreviewWeeklyStudyCalendar() {
    val thisWeeksDates: List<String> = getThisWeeksDates()
    val weeklyStudyDays =
        thisWeeksDates.mapIndexed { index, date ->
            WeeklyStudyDay(
                studyCount = listOf(3, 1, 2, 0, 0, 0, 0).getOrElse(index) { 0 },
                date = date,
            )
        }

    HY2Theme {
        WeeklyStudyCalendar(
            weeklyStudyDays = weeklyStudyDays,
            modifier = Modifier.background(Color.Black),
        )
    }
}

private fun getThisWeeksDates(date: LocalDate = LocalDate.now()): List<String> {
    val startOfWeek = date.with(ChronoUnit.DAYS.addTo(date, -(date.dayOfWeek.value - 1).toLong()))
    return List(7) { index ->
        val day = startOfWeek.plusDays(index.toLong())
        day.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN))
    }
}
