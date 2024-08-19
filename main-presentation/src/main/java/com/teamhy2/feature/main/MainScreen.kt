package com.teamhy2.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainRoute(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    MainScreen(
        onSettingClick = onSettingClick,
        onSeatingChartClick = onSeatingChartClick,
        modifier = modifier,
        viewModel = viewModel,
    )
}

@Composable
fun MainScreen(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Button(onClick = onSettingClick) {
            Text(text = "Setting")
        }
    }
}
