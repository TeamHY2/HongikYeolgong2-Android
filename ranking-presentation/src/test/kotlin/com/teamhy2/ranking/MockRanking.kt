package com.teamhy2.ranking

import com.teamhy2.ranking.model.DepartmentRanking

val mockDepartmentRankings =
    listOf(
        DepartmentRanking(
            department = "국어국문학과",
            studyDurationOfWeek = 200,
            currentRank = 1,
            rankChange = 1,
        ),
        DepartmentRanking(
            department = "디자인학부",
            studyDurationOfWeek = 170,
            currentRank = 2,
            rankChange = 1,
        ),
        DepartmentRanking(
            department = "경영학부",
            studyDurationOfWeek = 120,
            currentRank = 3,
            rankChange = -1,
        ),
        DepartmentRanking(
            department = "건축학부",
            studyDurationOfWeek = 80,
            currentRank = 4,
            rankChange = 1,
        ),
        DepartmentRanking(
            department = "불어불문학과",
            studyDurationOfWeek = 78,
            currentRank = 5,
            rankChange = -4,
        ),
        DepartmentRanking(
            department = "사회교육과",
            studyDurationOfWeek = 60,
            currentRank = 6,
            rankChange = 0,
        ),
        DepartmentRanking(
            department = "수학교육과",
            studyDurationOfWeek = 56,
            currentRank = 7,
            rankChange = 2,
        ),
        DepartmentRanking(
            department = "국어교육과",
            studyDurationOfWeek = 50,
            currentRank = 8,
            rankChange = 1,
        ),
        DepartmentRanking(
            department = "체육교육과",
            studyDurationOfWeek = 45,
            currentRank = 9,
            rankChange = -2,
        ),
        DepartmentRanking(
            department = "음악교육과",
            studyDurationOfWeek = 40,
            currentRank = 10,
            rankChange = 3,
        ),
    )
