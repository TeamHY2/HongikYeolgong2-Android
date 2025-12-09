package com.teamhy2.friend.domain.model

data class SearchedFriend(
    val friend: Friend,
    val friendStatus: FriendStatus,
    val canSendRequest: Boolean,
    val canCancelRequest: Boolean,
)
