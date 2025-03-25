package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.DeviceTokenResponse
import com.benenfeldt.remote.dto.UserInfoRequest
import com.benenfeldt.remote.dto.UserInfoResponse
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.dto.UserSignUpResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST("/api/v1/user/join")
    suspend fun signUp(
        @Body signUpRequest: UserSignUpRequest,
    ): Result<BaseResponse<UserSignUpResponse>>

    @DELETE("/api/v1/auth")
    suspend fun withdraw(): Result<BaseResponse<Unit>>

    @GET("/api/v1/user/me")
    suspend fun getUserInfo(): Result<BaseResponse<UserInfoResponse>>

    @PUT("/api/v1/user")
    suspend fun modifyUserInfo(
        @Body userInfoRequest: UserInfoRequest,
    ): Result<BaseResponse<UserInfoResponse>>

    @PUT("/api/v1/user/device-token")
    suspend fun updateDeviceToken(
        @Body deviceToken: String,
    ): Result<BaseResponse<DeviceTokenResponse?>>
}
