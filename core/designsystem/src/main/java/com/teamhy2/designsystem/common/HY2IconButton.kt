package com.teamhy2.designsystem.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

private const val BUTTON_ROUNDED_CORNER_SIZE = 4
private const val BUTTON_HEIGHT = 52

@Composable
fun HY2IconButton(
    text: String,
    @DrawableRes iconResId: Int,
    backgroundColor: Color = Blue100,
    textColor: Color = White,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ThrottleButton(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
            ),
        shape = RoundedCornerShape(BUTTON_ROUNDED_CORNER_SIZE),
        modifier =
            modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = textColor,
                modifier =
                    Modifier
                        .size(22.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = textColor,
                style = HY2Theme.typography.body02,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HY2IconButtonPreview() {
    HY2Theme {
        HY2IconButton(
            text = "열람실 이용 연장",
            iconResId = R.drawable.ic_clock,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HY2IconButtonDifferentColorPreview() {
    HY2Theme {
        HY2IconButton(
            text = "커스텀 배경색 버튼",
            iconResId = R.drawable.ic_calendar,
            backgroundColor = Gray600,
            textColor = Gray100,
            onClick = { },
        )
    }
}
