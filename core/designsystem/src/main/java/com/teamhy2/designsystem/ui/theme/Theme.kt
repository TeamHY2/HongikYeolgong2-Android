package com.teamhy2.designsystem.ui.theme

import android.app.Activity
import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = Blue100,
        secondary = Blue400,
        tertiary = Yellow100,
        background = Black,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Blue100,
        secondary = Blue400,
        tertiary = Yellow100,
        background = Black,
    )

private val LocalHY2Typography =
    staticCompositionLocalOf<HY2Typography> {
        error("No HY2Typography provided")
    }

/*
* HY2Theme
*
* Typo를 변경하고 싶다면 HY2Theme.typography.title01 방식으로 접근
* */
object HY2Theme {
    val typography: HY2Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalHY2Typography.current
}

@Composable
fun ProvideHY2Typography(
    typography: HY2Typography,
    content: @Composable () -> Unit,
) {
    val provideTypography = remember { typography.copy() }.apply { update(typography) }
    CompositionLocalProvider(
        LocalHY2Typography provides provideTypography,
        content = content,
    )
}

@Composable
fun HY2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            window.statusBarColor = Color.TRANSPARENT

            // 항상 다크테마이기 때문에 상태바 아이콘들을 밝은 톤으로 표시
            insetsController.isAppearanceLightStatusBars = false
        }
    }

    val typography = HY2Typography()

    ProvideHY2Typography(typography) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
