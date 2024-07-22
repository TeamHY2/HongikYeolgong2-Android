package com.teamhy2.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.presentation.Hy2Calendar
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HY2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Hy2Calendar(
                            calendar = Calendar(),
                            onPreviousMonthClick = { /* TODO: 구현 필요 */ },
                            onNextMonthClick = { /* TODO: 구현 필요 */ },
                            modifier = Modifier.padding(horizontal = 26.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        color = White,
        style = HY2Theme.typography.caption,
        modifier = modifier,
    )
}

@Preview(showBackground = false)
@Composable
fun GreetingPreview() {
    HY2Theme {
        Greeting("Android")
    }
}
