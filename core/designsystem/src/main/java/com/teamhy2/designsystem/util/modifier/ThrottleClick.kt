package com.teamhy2.designsystem.util.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

private const val DEFAULT_THROTTLE_TIME = 1000L

@Composable
fun Modifier.throttleClickable(
    throttleTime: Long = DEFAULT_THROTTLE_TIME,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    if (!enabled) return this

    val lastClickTime: MutableLongState = remember { mutableLongStateOf(0L) }

    return this.clickable {
        val currentTime: Long = System.currentTimeMillis()
        if (currentTime - lastClickTime.longValue < throttleTime) return@clickable
        onClick()
        lastClickTime.longValue = currentTime
    }
}
