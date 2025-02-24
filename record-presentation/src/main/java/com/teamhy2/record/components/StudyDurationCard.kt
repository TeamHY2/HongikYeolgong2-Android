package com.teamhy2.record.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.R.drawable.ic_calendar
import com.teamhy2.designsystem.R.drawable.ic_clock
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.record.presentation.R

@Composable
internal fun StudyDurationCard(
    title: String,
    studyDurationCardType: StudyDurationCardType,
    studyHours: Int,
    studyMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .aspectRatio(1.98f)
                .background(
                    brush = Brush.radialGradient(listOf(Color(0xFF23262D), Color(0x3323262D))),
                    shape = RoundedCornerShape(4.dp),
                )
                .border(
                    width = 1.dp,
                    color = Gray800,
                    shape = RoundedCornerShape(4.dp),
                ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(studyDurationCardType.icon),
                contentDescription = null,
                tint = Gray200,
            )
            Text(
                text = title + stringResource(studyDurationCardType.unit),
                style = HY2Typography().body04,
                color = Gray200,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.record_time_format, studyHours, studyMinutes),
            style = HY2Typography().title03,
            color = Gray100,
        )
    }
}

internal enum class StudyDurationCardType(
    @DrawableRes val icon: Int,
    @StringRes val unit: Int,
) {
    YEAR(icon = ic_clock, unit = R.string.record_unit_year),
    MONTH(icon = ic_calendar, unit = R.string.record_unit_month),
}

@Preview(showBackground = true)
@Composable
private fun StudyDurationCardPreview() {
    Surface(color = BackgroundBlack) {
        StudyDurationCard(
            title = "2025",
            studyDurationCardType = StudyDurationCardType.YEAR,
            studyHours = 200,
            studyMinutes = 4,
            modifier = Modifier.width(156.dp),
        )
    }
}
