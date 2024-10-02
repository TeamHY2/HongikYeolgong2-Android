package com.teamhy2.feature.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_star_0
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_star_1
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_star_2
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_star_3

private const val STAR_IMAGE_SIZE = 28

@Composable
fun WeeklyDayComponent(
    dayOfWeek: String,
    studyCount: Int,
    date: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 6.dp),
    ) {
        Text(
            text = dayOfWeek,
            style = HY2Typography().caption,
            color = textColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = getStarDrawableId(studyCount)),
            contentDescription = null,
            modifier =
                Modifier
                    .size(STAR_IMAGE_SIZE.dp),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = date,
            style = HY2Typography().caption,
            color = textColor,
        )
    }
}

private fun getStarDrawableId(studyCount: Int): Int {
    return when (studyCount) {
        1 -> ic_star_1
        2 -> ic_star_2
        3 -> ic_star_3
        else -> ic_star_0
    }
}

@Preview
@Composable
private fun PreviewWeeklyDayComponent() {
    WeeklyDayComponent(
        dayOfWeek = "월",
        studyCount = 3,
        date = "9/23",
        textColor = White,
    )
}
