package com.teamhy2.friend.search

import com.teamhy2.friend.search.model.SearchResultItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FriendSearchUiState(
    val query: String = "",
    val results: ImmutableList<SearchResultItemUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
)

sealed interface FriendSearchEffect {
    data class ShowSnackBar(val message: String) : FriendSearchEffect

    data object NavigateToFriend : FriendSearchEffect
}
