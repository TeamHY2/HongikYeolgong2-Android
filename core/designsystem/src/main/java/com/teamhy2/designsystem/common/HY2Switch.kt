package com.teamhy2.designsystem.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

@Composable
fun HY2Switch(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    checkedBackgroundColor: Color = Blue100,
    unCheckedBackgroundColor: Color = Gray200,
) {
    val background: Color by animateColorAsState(
        targetValue = if (isChecked) checkedBackgroundColor else unCheckedBackgroundColor,
        label = "backgroundColorAnimation",
    )
    val offset: Dp by animateDpAsState(
        targetValue = if (isChecked) 23.dp else 3.dp,
        label = "offsetAnimation",
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            Modifier
                .size(width = 50.dp, height = 50.dp)
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(background)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {
                    onCheckedChanged(isChecked.not())
                },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = offset)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(White),
        )
    }
}

@Preview
@Composable
private fun HY2SwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }

    HY2Theme {
        HY2Switch(isChecked, onCheckedChanged = { isChecked = it })
    }
}
