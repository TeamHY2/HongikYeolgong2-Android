package com.teamhy2.designsystem.common

import android.content.res.Configuration
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

@Composable
fun HY2WebView(
    titleText: String,
    url: String,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Black else White),
    ) {
        HY2WebViewHeader(
            titleText = titleText,
            onCloseButtonClick = onCloseButtonClick,
            isDarkTheme = isDarkTheme,
            modifier = Modifier.fillMaxWidth(),
        )
        HY2WebViewBody(
            url = url,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        )
    }
}

@Composable
fun HY2WebViewHeader(
    titleText: String,
    onCloseButtonClick: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isDarkTheme) Black else White
    val iconColor = if (isDarkTheme) White else Black
    val textColor = if (isDarkTheme) White else Black

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 14.dp, horizontal = 28.dp),
    ) {
        Text(
            text = titleText,
            style = HY2Theme.typography.title02,
            color = textColor,
            modifier = Modifier.align(Alignment.Center),
        )
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = stringResource(R.string.hy2_web_view_close_button),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(iconColor),
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = onCloseButtonClick),
        )
    }
}

@Composable
fun HY2WebViewBody(
    url: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun HY2WebViewHeaderLightThemePreview() {
    val isDarkTheme = isSystemInDarkTheme()

    HY2Theme {
        HY2WebViewHeader(
            titleText = "좌석",
            onCloseButtonClick = { },
            isDarkTheme,
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun HY2WebViewHeaderDarkThemePreview() {
    val isDarkTheme = isSystemInDarkTheme()

    HY2Theme {
        HY2WebViewHeader(
            titleText = "좌석",
            onCloseButtonClick = { },
            isDarkTheme,
        )
    }
}

// WebView javaScriptEnabled 활성화시 프리뷰가 보이지 않는 현상이 있습니다.
@Preview(showBackground = true)
@Composable
private fun HY2WebViewBodyPreview() {
    HY2Theme {
        HY2WebViewBody(
            url = "https://www.naver.com",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HY2WebViewPreview() {
    HY2Theme {
        HY2WebView(
            titleText = "좌석",
            url = "https://www.naver.com",
            onCloseButtonClick = { },
        )
    }
}
