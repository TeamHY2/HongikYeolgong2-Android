package com.hongikyeolgong2.calendar.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.designsystem.util.modifier.throttleClickable
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_0
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_1
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_2
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_3
import java.time.LocalDate

private const val DAY_SIZE_RATIO = 1f

@Composable
fun Day(
    studyDay: StudyDay,
    dayState: DayState,
    onDayClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayUi = DayUi.of(studyDay.studyRoomUsage)

    Box(
        modifier =
            modifier
                .aspectRatio(DAY_SIZE_RATIO)
                .alpha(
                    when (dayState) {
                        DayState.Default, DayState.Selected -> 1f
                        DayState.Unselected -> dayUi.unselectedAlpha
                    },
                )
                .throttleClickable(onClick = onDayClicked),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = dayUi.background),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxSize(),
        )
        Text(
            text = studyDay.date.dayOfMonth.toString(),
            style = HY2Typography().body03,
            color = dayUi.textColor,
        )
    }
}

enum class DayState {
    Default,
    Selected,
    Unselected,
}

enum class DayUi(
    val textColor: Color,
    @DrawableRes val background: Int,
    val unselectedAlpha: Float,
) {
    NEVER_USED(
        textColor = Gray300,
        background = bg_day_0,
        unselectedAlpha = 0.3f,
    ),
    USED_ONCE(
        textColor = Gray100,
        background = bg_day_1,
        unselectedAlpha = 0.2f,
    ),
    USED_ONCE_EXTENDED_ONCE(
        textColor = White,
        background = bg_day_2,
        unselectedAlpha = 0.1f,
    ),
    USED_ONCE_EXTENDED_TWICE(
        textColor = Gray600,
        background = bg_day_3,
        unselectedAlpha = 0.05f,
    ),
    ;

    companion object {
        fun of(studyRoomUsage: StudyRoomUsage): DayUi {
            return when (studyRoomUsage) {
                StudyRoomUsage.NEVER_USED -> NEVER_USED
                StudyRoomUsage.USED_ONCE -> USED_ONCE
                StudyRoomUsage.USED_ONCE_EXTENDED_ONCE -> USED_ONCE_EXTENDED_ONCE
                StudyRoomUsage.USED_ONCE_EXTENDED_TWICE -> USED_ONCE_EXTENDED_TWICE
            }
        }
    }
}

@Preview
@Composable
private fun NeverUsedDayPreview() {
    HY2Theme {
        Row {
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.NEVER_USED),
                dayState = DayState.Default,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(12.dp))
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.NEVER_USED),
                dayState = DayState.Unselected,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Preview
@Composable
private fun UsedOnceDayPreview() {
    HY2Theme {
        Row {
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE),
                dayState = DayState.Default,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(12.dp))
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE),
                dayState = DayState.Unselected,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Preview
@Composable
private fun UsedOnceExtendedOnceDayPreview() {
    HY2Theme {
        Row {
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                dayState = DayState.Default,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(12.dp))
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                dayState = DayState.Unselected,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Preview
@Composable
private fun UsedOnceExtendedTwiceDayPreview() {
    HY2Theme {
        Row {
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_TWICE),
                dayState = DayState.Default,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(12.dp))
            Day(
                studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_TWICE),
                dayState = DayState.Unselected,
                onDayClicked = {},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}
