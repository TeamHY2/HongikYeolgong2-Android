package com.teamhy2.friend.domain.repository

import com.teamhy2.friend.domain.model.DateType
import com.teamhy2.friend.domain.model.Friend

interface FriendRepository {
    suspend fun fetchFriends(dateType: DateType): Result<List<Friend>>
}
