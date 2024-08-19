package com.teamhy2.feature.main.webviews.inquiry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamhy2.designsystem.common.HY2WebView

@Composable
fun InquiryRoute(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InquiryScreen(
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}

@Composable
fun InquiryScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HY2WebView(
        titleText = "문의",
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
