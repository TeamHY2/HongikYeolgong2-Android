package com.teamhy2.ranking.repository

import com.teamhy2.ranking.model.Ranking
import com.teamhy2.ranking.model.WeekNumber
import java.time.LocalDate

interface RankingRepository {
    suspend fun fetchWeekNumber(date: LocalDate): Result<WeekNumber>

    suspend fun fetchRanking(weekNumber: Int): Result<Ranking>
}
