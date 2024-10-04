package com.teamhy2.feature.main.component

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

private val DAYS_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")
private const val DATE_FORMAT_PATTERN = "M/d"

@Composable
fun WeeklyCalendar(
    studyCounts: List<Int>,
    dates: List<String>,
    modifier: Modifier = Modifier,
) {
    val currentDate = LocalDate.now()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        DAYS_OF_WEEK.forEachIndexed { index, dayOfWeek ->
            val date = dates.getOrElse(index) { "" }
            val isFutureDate = isDateInFuture(date, currentDate)

            WeeklyDayComponent(
                dayOfWeek = dayOfWeek,
                studyCount = studyCounts.getOrElse(index) { 0 },
                date = date,
                textColor = if (isFutureDate) Gray400 else White,
            )
        }
    }
}

private fun isDateInFuture(
    dateText: String,
    currentDate: LocalDate,
): Boolean {
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
    val parsedMonthDay = formatter.parse(dateText)
    val month = parsedMonthDay[ChronoField.MONTH_OF_YEAR]
    val day = parsedMonthDay[ChronoField.DAY_OF_MONTH]
    val parsedDate = LocalDate.of(currentDate.year, month, day)

    return parsedDate.isAfter(currentDate)
}

@Preview(showBackground = true)
@Composable
private fun PreviewWeeklyCalendar() {
    val thisWeeksDates = getThisWeeksDates()
    HY2Theme {
        WeeklyCalendar(
            studyCounts = listOf(3, 1, 2, 0, 0, 0, 0),
            dates = thisWeeksDates,
            modifier =
                Modifier
                    .background(Color.Black),
        )
    }
}

private fun getThisWeeksDates(): List<String> {
    val today = LocalDate.now()
    val startOfWeek =
        today.with(ChronoUnit.DAYS.addTo(today, -(today.dayOfWeek.value - 1).toLong()))
    return List(7) { index ->
        val day = startOfWeek.plusDays(index.toLong())
        day.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN))
    }
}
