package com.hongikyeolgong2.calendar.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.model.StudyRoomUsage
import com.teamhy2.designsystem.ui.theme.Blue200
import com.teamhy2.designsystem.ui.theme.Blue400
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_0
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_1
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_2
import com.teamhy2.hongikyeolgong2.calendar.presentation.R.drawable.bg_day_3
import java.time.LocalDate

private const val DEFAULT_RADIUS = 8
private const val DAY_SIZE_RATIO = 1.212f

@Composable
fun Day(
    studyDay: StudyDay,
    modifier: Modifier = Modifier,
) {
    val dayUi = DayUi.of(studyDay.studyRoomUsage)

    Box(
        modifier =
            modifier
                .aspectRatio(DAY_SIZE_RATIO, false)
                .then(dayUi.modifier),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = dayUi.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        Text(
            text = studyDay.date.dayOfMonth.toString(),
            style = HY2Typography().body03,
            color = dayUi.textColor,
        )
    }
}

enum class DayUi(
    val textColor: Color,
    @DrawableRes val background: Int,
    val modifier: Modifier,
) {
    NEVER_USED(
        textColor = Gray300,
        background = bg_day_0,
        modifier =
            Modifier
                .clip(RoundedCornerShape(DEFAULT_RADIUS.dp)),
    ),
    USED_ONCE(
        textColor = Gray100,
        background = bg_day_1,
        modifier =
            Modifier
                .clip(RoundedCornerShape(DEFAULT_RADIUS.dp))
                .border(
                    width = 1.dp,
                    color = Blue400,
                    shape = RoundedCornerShape(DEFAULT_RADIUS.dp),
                ),
    ),
    USED_ONCE_EXTENDED_ONCE(
        textColor = White,
        background = bg_day_2,
        modifier =
            Modifier
                .clip(RoundedCornerShape(DEFAULT_RADIUS.dp))
                .border(width = 1.dp, color = Blue200, shape = RoundedCornerShape(DEFAULT_RADIUS.dp)),
    ),
    USED_ONCE_EXTENDED_TWICE(
        textColor = Gray600,
        background = bg_day_3,
        modifier =
            Modifier
                .clip(RoundedCornerShape(DEFAULT_RADIUS.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFCECF37),
                    shape = RoundedCornerShape(DEFAULT_RADIUS.dp),
                ),
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
        Day(
            studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.NEVER_USED),
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
private fun UsedOnceDayPreview() {
    HY2Theme {
        Day(
            studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE),
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
private fun UsedOnceExtendedOnceDayPreview() {
    HY2Theme {
        Day(
            studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
private fun UsedOnceExtendedTwiceDayPreview() {
    HY2Theme {
        Day(
            studyDay = StudyDay(LocalDate.now(), StudyRoomUsage.USED_ONCE_EXTENDED_TWICE),
            modifier = Modifier.size(40.dp),
        )
    }
}
