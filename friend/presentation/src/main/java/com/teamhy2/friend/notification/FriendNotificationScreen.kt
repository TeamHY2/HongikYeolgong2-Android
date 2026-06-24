package com.teamhy2.friend.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.friend.domain.model.FriendNotification
import com.teamhy2.friend.notification.component.FriendNotificationItem
import com.teamhy2.hongikyeolgong2.friend.presentation.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FriendNotificationRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: FriendNotificationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localSnackBar = LocalShowSnackBar.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FriendNotificationSideEffect.ShowSnackBar -> localSnackBar.showSnackBar(effect.message)
                FriendNotificationSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    FriendNotificationScreen(
        notifications = uiState.notifications,
        onBackClick = viewModel::onBackClicked,
        onAccept = viewModel::onAccept,
        onReject = viewModel::onReject,
        modifier = modifier,
    )
}

@Composable
fun FriendNotificationScreen(
    notifications: ImmutableList<FriendNotification>,
    onBackClick: () -> Unit,
    onAccept: (notificationId: Long, friendId: Long, senderId: Long) -> Unit,
    onReject: (notificationId: Long, friendId: Long, senderId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = 19.dp),
    ) {
        FriendNotificationHeader(
            modifier = Modifier.padding(start = 25.dp),
            onBackClick = onBackClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        FriendNotificationContent(
            modifier = Modifier.padding(horizontal = 25.dp),
            notifications = notifications,
            onAccept = onAccept,
            onReject = onReject,
        )
    }
}

@Composable
private fun FriendNotificationHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_left),
            contentDescription = "back",
            modifier =
                Modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
            tint = Gray200,
        )
        Text(text = "친구 알림", style = HY2Theme.typography.head, color = Gray100)
    }
}

@Composable
private fun FriendNotificationContent(
    modifier: Modifier = Modifier,
    notifications: ImmutableList<FriendNotification>,
    onAccept: (notificationId: Long, friendId: Long, senderId: Long) -> Unit,
    onReject: (notificationId: Long, friendId: Long, senderId: Long) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            notifications,
            key = { it.id },
        ) { item ->
            FriendNotificationItem(
                content = item.content,
                receivedAt = item.receivedAt,
                onAccept = { onAccept(item.id, item.receiverId, item.senderId) },
                onReject = { onReject(item.id, item.receiverId, item.senderId) },
            )
        }
    }
}

@Preview
@Composable
private fun FriendNotificationLoadedScreenPreview() {
    HY2Theme {
        FriendNotificationScreen(
            notifications =
                persistentListOf(
                    FriendNotification(
                        id = 1,
                        senderId = 2,
                        receiverId = 9,
                        content = "말하는감자님이 친구 요청을 보냈습니다.",
                        type = "REQUEST",
                        receivedAt = "30분",
                    ),
                    FriendNotification(
                        id = 2,
                        senderId = 3,
                        receiverId = 9,
                        content = "말하는고구마님이 친구 요청을 보냈습니다.",
                        type = "REQUEST",
                        receivedAt = "2시간",
                    ),
                ),
            onBackClick = {},
            onAccept = { _, _, _ -> },
            onReject = { _, _, _ -> },
        )
    }
}
