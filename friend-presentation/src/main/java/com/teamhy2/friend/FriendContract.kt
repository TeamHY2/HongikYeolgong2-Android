package com.teamhy2.friend

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.friend.domain.model.Friend

sealed interface FriendUiState : UiState {
    data object Loading : FriendUiState

    data class Loaded(val friends: List<Friend>) : FriendUiState
}

sealed interface FriendUiIntent : UiIntent {
    data object EnterFriendScreen : FriendUiIntent
}

sealed interface FriendSideEffect : SideEffect {
    data class ShowSnackBar(val message: String) : FriendSideEffect
}
