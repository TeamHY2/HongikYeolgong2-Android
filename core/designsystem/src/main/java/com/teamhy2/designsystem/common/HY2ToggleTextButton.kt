package com.teamhy2.designsystem.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White

@Composable
fun HY2ToggleTextButton(
    activatedButton: ActivatedToggleButton,
    leftText: String,
    rightText: String,
    onClick: (ActivatedToggleButton) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onClick(activatedButton.toggled()) },
        shape = RoundedCornerShape(6.dp),
        color = Gray800,
        modifier =
            modifier
                .size(width = 104.dp, 32.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(4.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .background(
                            color =
                                Blue100.takeIf { activatedButton == ActivatedToggleButton.LEFT }
                                    ?: Color.Transparent,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .weight(1f)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = leftText,
                    color =
                        White.takeIf { activatedButton == ActivatedToggleButton.LEFT }
                            ?: Gray100,
                    style = HY2Typography().body07,
                )
            }
            Box(
                modifier =
                    Modifier
                        .background(
                            color =
                                Blue100.takeIf { activatedButton == ActivatedToggleButton.RIGHT }
                                    ?: Color.Transparent,
                            shape = RoundedCornerShape(2.dp),
                        )
                        .weight(1f)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = rightText,
                    style = HY2Typography().body07,
                    color =
                        White.takeIf { activatedButton == ActivatedToggleButton.RIGHT }
                            ?: Gray100,
                )
            }
        }
    }
}

enum class ActivatedToggleButton {
    LEFT,
    RIGHT,
    ;

    fun toggled(): ActivatedToggleButton {
        return when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

@Preview
@Composable
private fun HY2ToggleTextButtonPreview() {
    HY2Theme {
        var activatedToggleButton by remember { mutableStateOf(ActivatedToggleButton.LEFT) }

        HY2ToggleTextButton(
            activatedButton = activatedToggleButton,
            leftText = "월간",
            rightText = "일간",
            onClick = { activatedToggleButton = it },
        )
    }
}
