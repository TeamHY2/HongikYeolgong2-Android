package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.RankingResponse
import com.benenfeldt.remote.dto.StudyDayRequest
import com.benenfeldt.remote.dto.StudyDayResponse
import com.benenfeldt.remote.dto.WeeklyStudyDayResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StudyService {
    @GET("/api/v1/study/ranking")
    suspend fun getRanking(
        @Query("yearWeek") weekNumber: Int,
    ): Result<BaseResponse<RankingResponse>>

    @GET("/api/v1/study/week")
    suspend fun getWeeklyStudyDay(): Result<BaseResponse<List<WeeklyStudyDayResponse>>>

    @POST("/api/v1/study")
    suspend fun postStudyDay(
        @Body studyDayRequest: StudyDayRequest,
    ): Result<BaseResponse<StudyDayResponse>>
}
