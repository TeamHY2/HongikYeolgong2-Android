package com.teamhy2.designsystem.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

const val DEFAULT_THROTTLE_TIME = 1000L

@Composable
fun ThrottledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    throttleTime: Long = DEFAULT_THROTTLE_TIME,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val throttledClick =
        remember(throttleTime) {
            var lastClickTime: Long = 0L
            {
                val currentTime: Long = System.currentTimeMillis()
                if (currentTime - lastClickTime >= throttleTime) {
                    onClick()
                    lastClickTime = currentTime
                }
            }
        }

    Button(
        onClick = throttledClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}
