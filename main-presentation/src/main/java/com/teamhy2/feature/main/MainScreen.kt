package com.teamhy2.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.feature.main.component.WiseSayingComponent
import com.teamhy2.feature.main.model.MainUiState

@Composable
fun MainRoute(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState: MainUiState = viewModel.mainUiState.collectAsState().value

    MainScreen(
        onSettingClick = onSettingClick,
        onSeatingChartClick = onSeatingChartClick,
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
fun MainScreen(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: MainUiState,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        WiseSayingComponent(
            quote = uiState.wiseSaying.quote,
            author = uiState.wiseSaying.author,
            modifier = Modifier,
        )
        Button(onClick = onSettingClick) {
            Text(text = "Setting")
        }
        Button(onClick = onSeatingChartClick) {
            Text(text = "SeatingChart")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val state by remember { mutableStateOf(MainUiState()) }

    HY2Theme {
        MainScreen(
            onSettingClick = { },
            onSeatingChartClick = { },
            uiState = state,
        )
    }
}
