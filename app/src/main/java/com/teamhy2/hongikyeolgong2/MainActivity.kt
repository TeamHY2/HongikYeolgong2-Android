package com.teamhy2.hongikyeolgong2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			HY2Theme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Greeting(
						name = "Android",
						modifier = Modifier.padding(innerPadding),
					)
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
