package com.teamhy2.friend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import kotlinx.coroutines.launch

@Composable
fun FriendRoute(
    modifier: Modifier = Modifier,
    friendViewModel: FriendViewModel = hiltViewModel(),
) {
    val friendState: FriendUiState by friendViewModel.uiState.collectAsStateWithLifecycle()
    val localShowSnackBar = LocalShowSnackBar.current

    LaunchedEffect(Unit) {
        friendViewModel.sendIntent(FriendUiIntent.EnterFriendScreen)
        launch {
            friendViewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is FriendSideEffect.ShowSnackBar -> localShowSnackBar.showSnackBar(sideEffect.message)
                }
            }
        }
    }

    FriendContent(
        state = friendState,
        modifier = modifier,
        onEvent = friendViewModel::sendIntent,
    )
}

@Composable
fun FriendContent(
    state: FriendUiState,
    modifier: Modifier = Modifier,
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
                    },
                    onAddFriendClick = {},
                    modifier = modifier,
                )
            } else {
                FriendScreen(
                    friends = state.friends,
                    selectedRecordFilterType = state.selectedRecordFilterType,
                    isNotificationOn = state.isNotificationOn,
                    onAddFriendClick = {},
                    onNotificationButtonClick = {
                        onEvent(FriendUiIntent.MarkNotificationsAsRead)
                    },
                    onRecordFilterClick = { recordFilterType ->
                        onEvent(FriendUiIntent.ChangeRecordFilterType(recordFilterType))
                    },
                    modifier = modifier,
                )
            }
    }
}
