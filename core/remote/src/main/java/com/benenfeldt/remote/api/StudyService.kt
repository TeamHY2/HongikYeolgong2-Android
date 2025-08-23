package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.CalendarStudyDayResponse
import com.benenfeldt.remote.dto.RankingResponse
import com.benenfeldt.remote.dto.StudyDurationResponse
import com.benenfeldt.remote.dto.StudyEndRequest
import com.benenfeldt.remote.dto.StudyEndResponse
import com.benenfeldt.remote.dto.StudyStartRequest
import com.benenfeldt.remote.dto.StudyStartResponse
import com.benenfeldt.remote.dto.StudyingUserResponse
import com.benenfeldt.remote.dto.WeeklyStudyDayResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface StudyService {
    @GET("/api/v1/study/ranking")
    suspend fun getRanking(
        @Query("yearWeek") weekNumber: Int,
    ): Result<BaseResponse<RankingResponse>>

    @GET("/api/v1/study/record/week")
    suspend fun getWeeklyStudyDay(): Result<BaseResponse<List<WeeklyStudyDayResponse>>>

    @GET("/api/v1/study/record/count-all")
    suspend fun getCalendarStudyDay(): Result<BaseResponse<List<CalendarStudyDayResponse>>>

    @GET("/api/v1/study/record/duration")
    suspend fun getStudyDuration(
        @Query("date") date: String? = null,
    ): Result<BaseResponse<StudyDurationResponse>>

    @POST("/api/v1/study/session/start")
    suspend fun postStudyStart(
        @Body studyStartRequest: StudyStartRequest,
    ): Result<BaseResponse<StudyStartResponse>>

    @PATCH("/api/v1/study/session/end")
    suspend fun patchStudyEnd(
        @Body studyEndRequest: StudyEndRequest,
    ): Result<BaseResponse<StudyEndResponse>>

    @GET("/api/v1/study/session")
    suspend fun getStudyingUsers(): Result<BaseResponse<List<StudyingUserResponse>>>
}
