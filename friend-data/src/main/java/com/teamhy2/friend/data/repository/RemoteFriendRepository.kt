package com.teamhy2.friend.data.repository

import com.benenfeldt.remote.api.FriendService
import com.benenfeldt.remote.dto.FriendStudyResponse
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.friend.data.mapper.toDomain
import com.teamhy2.friend.domain.model.DateType
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.repository.FriendRepository
import javax.inject.Inject

class RemoteFriendRepository
    @Inject
    constructor(
        private val friendService: FriendService,
    ) : FriendRepository {
        override suspend fun fetchFriends(dateType: DateType): Result<List<Friend>> {
            return friendService.getFriendsStudyTime(dateType.name).toResult { baseResponse ->
                baseResponse.data.map(FriendStudyResponse::toDomain)
            }
        }
    }
