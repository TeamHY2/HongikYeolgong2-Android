package com.teamhy2.feature.main.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun MainImageButton(
    imageResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
        onClick = onClick,
        modifier =
            modifier
                .paint(
                    painter = painterResource(id = imageResId),
                    contentScale = ContentScale.FillBounds,
                ),
    ) {
        Text(
            text = text,
            color = Color.White,
            style = HY2Typography().body02,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainImageButtonPreview() {
    MainImageButton(
        imageResId = R.drawable.img_seating_chart_button_background,
        text = "좌석",
        onClick = { },
    )
}
