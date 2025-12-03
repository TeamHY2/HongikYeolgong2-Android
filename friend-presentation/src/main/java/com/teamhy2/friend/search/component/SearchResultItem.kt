package com.teamhy2.friend.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.friend.domain.model.FriendStatus

@Composable
fun SearchResultItem(
    modifier: Modifier = Modifier,
    nickname: String,
    status: FriendStatus,
    onStatusButtonClick: ((FriendStatus) -> Unit),
) {
    val friendRequestStatusButtonBackgroundColor =
        when (status) {
            FriendStatus.NONE -> Blue100
            FriendStatus.PENDING -> Gray400
            else -> Gray400
        }

    val friendRequestStatusButtonTextColor =
        when (status) {
            FriendStatus.NONE -> White
            FriendStatus.PENDING -> Gray200
            else -> Gray200
        }

    val friendRequestStatusButtonText =
        when (status) {
            FriendStatus.NONE -> "친구 요청"
            FriendStatus.PENDING -> "친구요청됨"
            else -> ""
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = nickname,
            style = HY2Theme.typography.body05,
            color = Gray100,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (status == FriendStatus.NONE || status == FriendStatus.PENDING) {
            Box(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .background(
                            color = friendRequestStatusButtonBackgroundColor,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .clickable {
                            onStatusButtonClick(status)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = friendRequestStatusButtonText,
                    style = HY2Theme.typography.body07,
                    color = friendRequestStatusButtonTextColor,
                )
            }
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun PreviewSearchResultItem() {
    HY2Theme {
        Column {
            SearchResultItem(
                nickname = "말하는감자",
                status = FriendStatus.NONE,
                onStatusButtonClick = { _ -> },
            )
            Spacer(modifier = Modifier.height(10.dp))
            SearchResultItem(
                nickname = "말하는감자",
                status = FriendStatus.PENDING,
                onStatusButtonClick = { _ -> },
            )
            Spacer(modifier = Modifier.height(10.dp))
            SearchResultItem(
                nickname = "이미친구",
                status = FriendStatus.ACCEPTED,
                onStatusButtonClick = { _ -> },
            )
        }
    }
}
