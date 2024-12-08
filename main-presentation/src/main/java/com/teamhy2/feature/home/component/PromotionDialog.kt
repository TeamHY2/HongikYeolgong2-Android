package com.teamhy2.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme

@Composable
fun PromotionDialog(
    promotionImageUrl: String,
    onDetailClick: () -> Unit,
    onCloseClick: () -> Unit,
    onCloseTodayClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.75f)
        Surface(
            modifier =
                modifier
                    .width(screenWidth - 30.dp * 2)
                    .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.Transparent,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent,
                ) {
                    AsyncImage(
                        model = promotionImageUrl,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDetailClick()
                                },
                        onState = { state ->
                            if (state is AsyncImagePainter.State.Error) {
                                onDismiss()
                            }
                        },
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    val interactionSource: MutableInteractionSource =
                        remember { MutableInteractionSource() }

                    Text(
                        text = "오늘 그만 보기",
                        color = Gray300,
                        style = HY2Theme.typography.body05,
                        modifier =
                            Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { onCloseTodayClick() },
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "|",
                        color = Gray300,
                        style = HY2Theme.typography.body05,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "닫기",
                        color = Gray300,
                        style = HY2Theme.typography.body05,
                        modifier =
                            Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { onCloseClick() },
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PromotionDialogPreview() {
    HY2Theme {
        PromotionDialog(
            promotionImageUrl = "https://github.com/user-attachments/assets/e268f0b3-df44-4498-b23a-5325c1d6ef85",
            onDetailClick = {},
            onCloseClick = {},
            onCloseTodayClick = {},
            onDismiss = {},
        )
    }
}
