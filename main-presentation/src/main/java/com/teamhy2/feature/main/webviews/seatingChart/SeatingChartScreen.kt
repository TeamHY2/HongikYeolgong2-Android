package com.teamhy2.feature.main.webviews.seatingChart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamhy2.designsystem.common.HY2WebView

@Composable
fun SeatingChartRoute(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SeatingChartScreen(
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}

@Composable
fun SeatingChartScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HY2WebView(
        titleText = "좌석",
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
