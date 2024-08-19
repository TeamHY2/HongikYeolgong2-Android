package com.teamhy2.feature.main.webviews.notice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamhy2.designsystem.common.HY2WebView

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
        titleText = "공지",
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
