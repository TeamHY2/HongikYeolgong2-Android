package com.teamhy2.friend.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.hongikyeolgong2.friend.presentation.R

@Composable
fun NotificationButton(
    isNotificationOn: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Image(
            painter =
                painterResource(
                    R.drawable.ic_notification_on.takeIf { isNotificationOn }
                        ?: R.drawable.ic_notification_off,
                ),
            contentDescription = "알림",
        )
    }
}

class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview
@Composable
private fun NotificationButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isNotificationOn: Boolean,
) {
    HY2Theme {
        Column {
            NotificationButton(
                isNotificationOn = isNotificationOn,
                onClick = {},
            )
        }
    }
}
