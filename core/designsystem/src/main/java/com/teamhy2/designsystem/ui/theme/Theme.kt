package com.teamhy2.designsystem.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
	primary = Blue100,
	secondary = Blue400,
	tertiary = Yellow100,
	background = Black
)

private val LightColorScheme = lightColorScheme(
	primary = Blue100,
	secondary = Blue400,
	tertiary = Yellow100,
	background = Black
)

@Composable
fun HongikYeolgong2Theme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
	// dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			val insetsController = WindowCompat.getInsetsController(window, view)
			window.statusBarColor = colorScheme.background.toArgb()

			// 항상 다크테마이기 때문에 상태바 아이콘들을 밝은 톤으로 표시
			insetsController.isAppearanceLightStatusBars = false
			insetsController.isAppearanceLightNavigationBars = false
		}
	}
	val shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(4.dp))

	MaterialTheme(
		colorScheme = colorScheme,
		shapes = shapes,
		typography = Typography,
		content = content,
	)
}
