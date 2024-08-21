package com.teamhy2.feature.main.webviews.inquiry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamhy2.designsystem.common.HY2WebView
import com.teamhy2.hongikyeolgong2.main.presentation.R

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
        titleText = stringResource(R.string.inquiry_title),
        url = url,
        onCloseButtonClick = onCloseButtonClick,
        modifier = modifier,
    )
}
