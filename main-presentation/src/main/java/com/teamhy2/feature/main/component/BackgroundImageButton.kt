package com.teamhy2.feature.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun BackgroundImageButton(
    imageResId: Int,
    text: String,
    textColor: Color = White,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = imageResId),
                    contentScale = ContentScale.FillBounds,
                )
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = HY2Typography().body02,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundImageButtonPreview() {
    BackgroundImageButton(
        imageResId = R.drawable.img_seating_chart_button_background,
        text = "좌석",
        onClick = { },
        modifier =
            Modifier
                .height(72.dp)
                .width(150.dp),
    )
}
