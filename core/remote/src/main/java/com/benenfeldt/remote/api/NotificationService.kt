package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.NotificationResponse
import retrofit2.http.GET

interface NotificationService {
    @GET("/api/v2/notification")
    suspend fun getNotifications(): Result<BaseResponse<List<NotificationResponse>>>
}
