package com.teamhy2.friend.data.mapper

import com.benenfeldt.remote.dto.FriendSearchResponse
import com.benenfeldt.remote.dto.FriendStatusResponse
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.model.SearchedFriend
import kotlin.time.Duration.Companion.seconds

internal fun FriendSearchResponse.toDomain(): SearchedFriend =
    SearchedFriend(
        friend =
            Friend(
                id = userId,
                userId = userId,
                nickname = nickname,
                studyTime = 0.seconds,
            ),
        friendStatus = friendStatus.toDomain(),
        canSendRequest = canSendRequest,
        canCancelRequest = canCancelRequest,
    )

internal fun FriendStatusResponse.toDomain(): FriendStatus =
    when (this) {
        FriendStatusResponse.PENDING -> FriendStatus.PENDING
        FriendStatusResponse.ACCEPTED -> FriendStatus.ACCEPTED
        FriendStatusResponse.REJECTED -> FriendStatus.REJECTED
        FriendStatusResponse.NONE -> FriendStatus.NONE
        FriendStatusResponse.CANCELED -> FriendStatus.CANCELED
    }
