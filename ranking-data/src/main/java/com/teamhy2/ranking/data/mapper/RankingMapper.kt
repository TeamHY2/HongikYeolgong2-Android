package com.teamhy2.ranking.data.mapper

import com.benenfeldt.remote.dto.DepartmentRankingResponse
import com.benenfeldt.remote.dto.RankingResponse
import com.teamhy2.ranking.model.DepartmentRanking
import com.teamhy2.ranking.model.Ranking

fun RankingResponse.toDomain(): Ranking {
    return Ranking(
        weekName = weekName,
        departmentRankings = departmentRankings.map { it.toDomain() },
    )
}

fun DepartmentRankingResponse.toDomain(): DepartmentRanking {
    return DepartmentRanking(
        department = department,
        studyDurationOfWeek = studyDurationOfWeek,
        currentRank = currentRank,
        rankChange = rankChange,
    )
}
