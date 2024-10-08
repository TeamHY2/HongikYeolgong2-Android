package com.teamhy2.designsystem.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.HY2Theme

@Composable
fun HY2LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(BackgroundBlack), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun HY2LoadingScreenPreview() {
    HY2Theme {
        HY2LoadingScreen()
    }
}
