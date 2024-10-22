package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.DefaultResponse
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.dto.UserSignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/v1/user/join")
    suspend fun signUp(
        @Body signUpRequest: UserSignUpRequest,
    ): Result<DefaultResponse<UserSignUpResponse>>
}
