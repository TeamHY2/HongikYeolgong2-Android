package com.teamhy2.friend.domain.repository

import com.teamhy2.friend.domain.model.Friend

interface FriendRepository {
    suspend fun fetchFriends(): Result<List<Friend>>
}
