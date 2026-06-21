package com.teamhy2.friend.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White

@Composable
fun FriendNotificationItem(
    content: String,
    receivedAt: String,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Gray800, shape = RoundedCornerShape(4.dp))
                .padding(top = 16.dp, bottom = 18.dp, start = 20.dp, end = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = content,
                style = HY2Theme.typography.body05,
                color = Gray100,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = receivedAt, style = HY2Theme.typography.caption, color = Gray300)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            HY2Button(
                text = "삭제",
                onClick = onReject,
                backgroundColor = Gray400,
                textColor = White,
                textStyle = HY2Theme.typography.body07,
                modifier =
                    Modifier
                        .weight(1f)
                        .height(34.dp),
            )
            HY2Button(
                text = "수락",
                onClick = onAccept,
                backgroundColor = Blue100,
                textColor = White,
                textStyle = HY2Theme.typography.body07,
                modifier =
                    Modifier
                        .weight(1f)
                        .height(34.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriendNotificationItem() {
    HY2Theme {
        FriendNotificationItem(
            content = "말하는감자님이 친구 요청을 보냈습니다.",
            receivedAt = "30분",
            onAccept = {},
            onReject = {},
        )
    }
}
