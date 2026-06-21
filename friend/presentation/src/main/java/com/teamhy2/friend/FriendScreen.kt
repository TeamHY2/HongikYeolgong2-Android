package com.teamhy2.friend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar

@Composable
fun FriendRoute(
    modifier: Modifier = Modifier,
    onAddFriendClick: () -> Unit,
    onNotificationClick: () -> Unit,
    friendViewModel: FriendViewModel = hiltViewModel(),
) {
    val friendState: FriendUiState by friendViewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalShowSnackBar.current

    LaunchedEffect(Unit) {
        friendViewModel.sendIntent(FriendUiIntent.EnterFriendScreen)
        friendViewModel.sideEffect.collect { effect ->
            when (effect) {
                is FriendSideEffect.ShowSnackBar -> showSnackBar.showSnackBar(effect.message)
            }
        }
    }

    FriendContent(
        state = friendState,
        modifier = modifier,
        onAddFriendClick = onAddFriendClick,
        onNotificationClick = onNotificationClick,
        onEvent = friendViewModel::sendIntent,
    )
}

@Composable
fun FriendContent(
    state: FriendUiState,
    modifier: Modifier = Modifier,
    onAddFriendClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onEvent: (FriendUiIntent) -> Unit,
) {
    when (state) {
        FriendUiState.Loading -> HY2CircularLoading()
        is FriendUiState.Loaded ->
            if (state.friends.isEmpty()) {
                EmptyFriendScreen(
                    isNotificationOn = state.isNotificationOn,
                    onNotificationClick = {
                        onEvent(FriendUiIntent.MarkNotificationsAsRead)
                        onNotificationClick()
                    },
                    onAddFriendClick = onAddFriendClick,
                    modifier = modifier,
                )
            } else {
                FriendScreen(
                    friends = state.friends,
                    selectedRecordFilterType = state.selectedRecordFilterType,
                    isNotificationOn = state.isNotificationOn,
                    onAddFriendClick = onAddFriendClick,
                    onNotificationButtonClick = {
                        onEvent(FriendUiIntent.MarkNotificationsAsRead)
                        onNotificationClick()
                    },
                    onRecordFilterClick = { recordFilterType ->
                        onEvent(FriendUiIntent.ChangeRecordFilterType(recordFilterType))
                    },
                    modifier = modifier,
                )
            }
    }
}
