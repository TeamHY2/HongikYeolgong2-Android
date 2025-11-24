package com.teamhy2.friend

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.common.HY2Spacer
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.friend.component.NotificationButton
import com.teamhy2.hongikyeolgong2.friend.presentation.R

@Composable
fun EmptyFriendScreen(
    isNotificationOn: Boolean,
    onNotificationClick: () -> Unit,
    onAddFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 36.dp, horizontal = 32.dp),
    ) {
        Row {
            Spacer(Modifier.weight(1f))
            NotificationButton(
                isNotificationOn = isNotificationOn,
                onClick = onNotificationClick,
            )
        }
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_not_found_big),
                contentDescription = null,
                modifier = Modifier.offset(x = 12.dp),
            )
            HY2Spacer(32)
            Text(
                text = "친구를 추가해\n공부 현황을 살펴보세요",
                color = Gray200,
                style = HY2Typography().title03,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.weight(1f))
        HY2Button(
            text = "친구 추가하기",
            onClick = onAddFriendClick,
            textColor = Gray100,
            backgroundColor = Gray600,
            modifier = Modifier.height(52.dp),
        )
    }
}

@Preview
@Composable
private fun EmptyFriendScreenPreview() {
    HY2Theme {
        EmptyFriendScreen(
            isNotificationOn = true,
            onNotificationClick = {},
            onAddFriendClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
