package com.teamhy2.friend.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.friend.domain.model.Friend
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun FriendItem(
    rankNumber: Int,
    friend: Friend,
) {
    Surface(
        color = Gray800,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.height(52.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.width(24.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(text = rankNumber.toString(), style = HY2Typography().body03, color = Gray100)
            }
            Text(text = friend.nickname, style = HY2Typography().body05, color = Gray100)
            Spacer(Modifier.weight(1f))
            friend.studyTime.toComponents { hours, minutes, _, _ ->
                Text(
                    text = "${hours}H ${minutes}M".takeIf { hours > 0 } ?: "${minutes}M",
                    style = HY2Typography().caption,
                    color = Gray100,
                )
            }
        }
    }
}

class FriendItemPreviewParameterProvider : PreviewParameterProvider<Pair<Int, Friend>> {
    override val values =
        sequenceOf(
            1 to
                Friend(
                    id = 1, userId = 1, nickname = "공부시간 0인 친구", studyTime = 0.seconds,
                ),
            10 to
                Friend(
                    id = 10,
                    userId = 10,
                    nickname = "두자릿수 랭킹 친구",
                    studyTime = (3600 * 50 + 300).seconds,
                ),
        )
}

@Preview
@Composable
private fun FriendItemPreview(
    @PreviewParameter(FriendItemPreviewParameterProvider::class) params: Pair<Int, Friend>,
) {
    HY2Theme {
        val (rankNumber, friend) = params
        FriendItem(rankNumber = rankNumber, friend = friend)
    }
}
