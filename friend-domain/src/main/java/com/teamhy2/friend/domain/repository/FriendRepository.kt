package com.teamhy2.friend.domain.repository

import com.teamhy2.friend.domain.model.DateType
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.model.SearchedFriend

interface FriendRepository {
    suspend fun fetchFriends(dateType: DateType): Result<List<Friend>>

    suspend fun searchFriends(nickname: String): Result<List<SearchedFriend>>

    suspend fun addNewFriend(receiverId: Long): Result<FriendStatus>
}
