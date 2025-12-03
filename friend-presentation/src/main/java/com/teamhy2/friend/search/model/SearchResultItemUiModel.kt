package com.teamhy2.friend.search.model

import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.model.FriendStatus

data class SearchResultItemUiModel(
    val friend: Friend,
    val friendStatus: FriendStatus,
)
