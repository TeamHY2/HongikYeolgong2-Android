package com.teamhy2.friend.domain.model

data class SearchedFriend(
    val userId: Long,
    val nickname: String,
    val friendStatus: FriendStatus,
)
