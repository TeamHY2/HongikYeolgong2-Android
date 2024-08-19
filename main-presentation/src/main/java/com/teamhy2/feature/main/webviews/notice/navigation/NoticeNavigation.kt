package com.teamhy2.feature.main.webviews.notice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.main.webviews.notice.NoticeRoute
import com.teamhy2.feature.main.webviews.notice.navigation.Notice.ROUTE

fun NavController.navigateToNotice() {
    navigate(ROUTE)
}

fun NavGraphBuilder.noticeScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
) {
    composable(route = ROUTE) {
        NoticeRoute(
            url = url,
            onCloseButtonClick = onCloseButtonClick,
        )
    }
}

object Notice {
    const val ROUTE = "notice"
}
