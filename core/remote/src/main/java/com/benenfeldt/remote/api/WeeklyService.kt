package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.WeekNumberResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface WeeklyService {
    @GET("/api/v1/week-field")
    suspend fun getWeekNumber(
        @Query("date") date: LocalDate,
    ): Result<BaseResponse<WeekNumberResponse>>
}
