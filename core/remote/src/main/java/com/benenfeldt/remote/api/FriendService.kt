package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.FriendStudyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {
    @GET("/api/v2/friends/study")
    suspend fun getFriendsStudyTime(
        @Query("dateType") dateType: String,
    ): Result<BaseResponse<List<FriendStudyResponse>>>
}
