package com.teamhy2.friend.search

import com.teamhy2.friend.search.model.SearchResultItemUiModel

data class FriendSearchUiState(
    val query: String = "",
    val results: List<SearchResultItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
)

sealed interface FriendSearchEffect {
    data class ShowSnackBar(val message: String) : FriendSearchEffect

    data object NavigateToFriend : FriendSearchEffect
}
