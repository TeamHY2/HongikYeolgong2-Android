package com.teamhy2.friend.data.repository

import com.benenfeldt.remote.api.FriendService
import com.benenfeldt.remote.dto.AddFriendRequest
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.friend.data.mapper.toDomain
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.model.SearchedFriend
import com.teamhy2.friend.domain.repository.FriendRepository
import javax.inject.Inject

class RemoteFriendRepository
    @Inject
    constructor(
        private val friendService: FriendService,
    ) : FriendRepository {
        override suspend fun fetchFriends(): Result<List<Friend>> {
            return Result.success(emptyList())
        }

        override suspend fun searchFriends(nickname: String): Result<List<SearchedFriend>> =
            friendService.searchFriends(nickname).toResult { response ->
                response.data.map { it.toDomain() }
            }

        override suspend fun addNewFriend(receiverId: Long): Result<FriendStatus> =
            friendService.addFriend(AddFriendRequest(receiverId))
                .toResult { it.data.friendStatus.toDomain() }
    }
