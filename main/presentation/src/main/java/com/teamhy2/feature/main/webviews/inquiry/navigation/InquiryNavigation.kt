package com.teamhy2.feature.main.webviews.inquiry.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.main.webviews.inquiry.InquiryRoute
import com.teamhy2.feature.main.webviews.inquiry.navigation.Inquiry.ROUTE

fun NavController.navigateToInquiry() {
    navigate(ROUTE)
}

fun NavGraphBuilder.inquiryScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
) {
    composable(route = ROUTE) {
        InquiryRoute(
            url = url,
            onCloseButtonClick = onCloseButtonClick,
        )
    }
}

object Inquiry {
    const val ROUTE = "inquiry"
}
