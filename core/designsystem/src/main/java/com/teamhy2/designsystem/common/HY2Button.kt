package com.teamhy2.designsystem.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

private const val ROUNDED_CORNER_SIZE = 4
private const val PADDING_SIZE = 8

@Composable
fun HY2Button(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Blue100,
    textColor: Color = White,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
            ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(PADDING_SIZE.dp)
                .clip(RoundedCornerShape(ROUNDED_CORNER_SIZE.dp)),
    ) {
        Text(
            text = text,
            color = textColor,
            style = HY2Theme.typography.body02,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HY2ButtonPreview() {
    HY2Theme {
        Column {
            HY2Button(
                text = "열람실 이용 연장",
                onClick = { /* TODO */ },
            )
            HY2Button(
                text = "커스텀 배경색 버튼",
                backgroundColor = Gray600,
                textColor = Gray100,
                onClick = { /* TODO */ },
            )
        }
    }
}
