package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.AddFriendRequest
import com.benenfeldt.remote.dto.AddFriendResponse
import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.FriendSearchResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendService {
    @GET("/api/v2/friends")
    suspend fun searchFriends(
        @Query("nickname") nickname: String,
    ): Result<BaseResponse<List<FriendSearchResponse>>>

    @POST("/api/v2/friends")
    suspend fun addFriend(
        @Body request: AddFriendRequest,
    ): Result<BaseResponse<AddFriendResponse>>

    @GET("/api/v2/friends/study")
    suspend fun getFriendsStudyTime(
        @Query("dateType") dateType: String,
    ): Result<BaseResponse<List<FriendStudyResponse>>>
}
