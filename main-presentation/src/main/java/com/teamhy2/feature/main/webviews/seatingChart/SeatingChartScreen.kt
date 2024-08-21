package com.teamhy2.feature.main.webviews.seatingChart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamhy2.designsystem.common.HY2WebView
import com.teamhy2.hongikyeolgong2.main.presentation.R

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
        titleText = stringResource(R.string.seating_chart_title),
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
