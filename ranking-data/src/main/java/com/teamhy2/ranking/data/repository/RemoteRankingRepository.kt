package com.teamhy2.ranking.data.repository

import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.api.WeeklyService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.ranking.data.mapper.toDomain
import com.teamhy2.ranking.model.Ranking
import com.teamhy2.ranking.model.WeekNumber
import com.teamhy2.ranking.repository.RankingRepository
import java.time.LocalDate
import javax.inject.Inject

class RemoteRankingRepository
    @Inject
    constructor(
        private val weeklyService: WeeklyService,
        private val studyService: StudyService,
    ) : RankingRepository {
        override suspend fun fetchWeekNumber(date: LocalDate): Result<WeekNumber> {
            return weeklyService.getWeekNumber(date).toResult { baseResponse ->
                baseResponse.data.toDomain()
            }
        }

        override suspend fun fetchRanking(weekNumber: Int): Result<Ranking> {
            return studyService.getRanking(weekNumber).toResult { baseResponse ->
                baseResponse.data.toDomain().copy(
                    departmentRankings = baseResponse.data.toDomain().departmentRankings.sortedBy { it.currentRank },
                )
            }
        }
    }
