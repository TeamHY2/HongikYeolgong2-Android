package com.teamhy2.friend.data.repository

import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.repository.FriendRepository
import javax.inject.Inject

class RemoteFriendRepository
    @Inject
    constructor() : FriendRepository {
        override suspend fun fetchFriends(): Result<List<Friend>> {
            return Result.success(emptyList())
        }
    }
