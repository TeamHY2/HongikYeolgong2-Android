package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.AddFriendRequest
import com.benenfeldt.remote.dto.AddFriendResponse
import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.CancelFriendRequest
import com.benenfeldt.remote.dto.FriendSearchResponse
import com.benenfeldt.remote.dto.FriendStudyResponse
import com.benenfeldt.remote.dto.UpdateFriendStatusRequest
import com.benenfeldt.remote.dto.UpdateFriendStatusResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @PATCH("/api/v2/friends/cancel")
    suspend fun cancelFriend(
        @Body request: CancelFriendRequest,
    ): Result<BaseResponse<JsonElement?>>

    @PATCH("/api/v2/friends")
    suspend fun updateFriendStatus(
        @Body request: UpdateFriendStatusRequest,
    ): Result<BaseResponse<UpdateFriendStatusResponse>>

    @GET("/api/v2/friends/study")
    suspend fun getFriendsStudyTime(
        @Query("dateType") dateType: String,
    ): Result<BaseResponse<List<FriendStudyResponse>>>
}
