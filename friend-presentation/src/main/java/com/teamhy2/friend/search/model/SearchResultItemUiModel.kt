package com.teamhy2.friend.search.model

import com.teamhy2.friend.domain.model.FriendStatus

data class SearchResultItemUiModel(
    val userId: Long,
    val nickname: String,
    val friendStatus: FriendStatus,
)
