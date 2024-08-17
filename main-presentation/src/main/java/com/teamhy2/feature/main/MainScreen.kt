package com.teamhy2.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainRoute(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MainScreen(
        onSettingClick = onSettingClick,
        onSeatingChartClick = onSeatingChartClick,
        modifier = modifier,
    )
}

@Composable
fun MainScreen(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Button(onClick = onSettingClick) {
            Text(text = "Setting")
        }
    }
}
