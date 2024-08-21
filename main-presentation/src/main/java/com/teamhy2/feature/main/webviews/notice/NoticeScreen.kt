package com.teamhy2.feature.main.webviews.notice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamhy2.designsystem.common.HY2WebView
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun NoticeRoute(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NoticeScreen(
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}

@Composable
fun NoticeScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HY2WebView(
        titleText = stringResource(R.string.notice_title),
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
