package com.teamhy2.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.feature.main.model.MainUiState
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.main.domain.model.WiseSaying

private const val BUTTON_HEIGHT = 52
private const val TOP_MARGIN = 40

@Composable
fun InitTimerComponent(
    onSeatingChartClick: () -> Unit,
    onStudyRoomStartClick: () -> Unit,
    uiState: MainUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        WiseSayingComponent(
            quote = uiState.wiseSaying.quote,
            author = uiState.wiseSaying.author,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier =
                Modifier
                    .height(BUTTON_HEIGHT.dp)
                    .fillMaxWidth(),
        ) {
            BackgroundImageButton(
                imageResId = R.drawable.img_seating_chart_button_background,
                text = stringResource(R.string.main_seating_chart),
                textColor = White.copy(alpha = 0.8f),
                onClick = onSeatingChartClick,
                modifier = Modifier.weight(70f),
            )
            Spacer(modifier = Modifier.weight(12f))
            BackgroundImageButton(
                imageResId = R.drawable.img_study_room_start_button_background,
                text = stringResource(R.string.main_start_study_room),
                onClick = onStudyRoomStartClick,
                modifier = Modifier.weight(230f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    val dummyUiState =
        MainUiState(
            wiseSaying =
                WiseSaying(
                    quote = "삶이 아무리 어려워 보일지라도\n항상 당신이 할 수 있고 성공할 수 있는 일이 있습니다.",
                    author = "스티븐 호킹",
                ),
        )

    HY2Theme {
        InitTimerComponent(
            onSeatingChartClick = { },
            onStudyRoomStartClick = { },
            uiState = dummyUiState,
            modifier =
                Modifier
                    .background(Black)
                    .height(308.dp),
        )
    }
}
