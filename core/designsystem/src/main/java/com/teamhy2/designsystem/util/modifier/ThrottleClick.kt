package com.teamhy2.designsystem.util.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

const val DEFAULT_THROTTLE_TIME = 1000L

@Composable
fun Modifier.throttleClick(
    throttleTime: Long = DEFAULT_THROTTLE_TIME,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val lastClickTime: MutableLongState = remember { mutableLongStateOf(0L) }

    return if (enabled) {
        this.clickable {
            val currentTime: Long = System.currentTimeMillis()
            if (currentTime - lastClickTime.longValue >= throttleTime) {
                onClick()
                lastClickTime.longValue = currentTime
            }
        }
    } else {
        this
    }
}
