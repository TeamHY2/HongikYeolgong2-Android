package com.teamhy2.designsystem.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

private val DIALOG_MARGIN = 30.dp
private val DIALOG_CORNER_RADIUS = RoundedCornerShape(8.dp)

@Composable
fun HY2Dialog(
    description: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gray800,
    leftButtonColor: Color = Blue100,
    rightButtonColor: Color = Gray600,
    leftButtonTextColor: Color = White,
    rightButtonTextColor: Color = Gray200,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        Surface(
            modifier =
                modifier
                    .width(screenWidth - DIALOG_MARGIN * 2)
                    .wrapContentHeight(),
            shape = DIALOG_CORNER_RADIUS,
            color = backgroundColor,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(start = 24.dp, top = 40.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = description,
                    color = Gray100,
                    style = HY2Theme.typography.title02,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    HY2DialogButton(
                        text = rightButtonText,
                        onClick = onRightButtonClick,
                        buttonColor = rightButtonColor,
                        textColor = rightButtonTextColor,
                        modifier = modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    HY2DialogButton(
                        text = leftButtonText,
                        onClick = onLeftButtonClick,
                        buttonColor = leftButtonColor,
                        textColor = leftButtonTextColor,
                        modifier = modifier.weight(1f),
                    )
                }
            }
        }
    }
}

private val BUTTON_CORNER_RADIUS = 8.dp
private val BUTTON_HEIGHT = 46.dp

@Composable
private fun HY2DialogButton(
    text: String,
    onClick: () -> Unit,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier =
            modifier
                .height(BUTTON_HEIGHT),
    ) {
        Text(
            text = text,
            color = textColor,
            style = HY2Theme.typography.title03,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun HY2DialogPreview() {
    HY2Theme {
        HY2Dialog(
            description = "로그아웃 하실 건가요?",
            leftButtonText = "돌아가기",
            rightButtonText = "로그아웃하기",
            onLeftButtonClick = { /* TODO */ },
            onRightButtonClick = { /* TODO */ },
            onDismiss = { /* TODO */ },
        )
    }
}
