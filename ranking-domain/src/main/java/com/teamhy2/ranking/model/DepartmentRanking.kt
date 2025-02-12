package com.teamhy2.ranking.model

data class DepartmentRanking(
    val department: String,
    val studyDurationOfWeek: Int,
    val currentRank: Int,
    val rankChange: Int,
) {
    companion object {
        val DEFAULT =
            DepartmentRanking(
                department = "",
                studyDurationOfWeek = 0,
                currentRank = 0,
                rankChange = 0,
            )

        fun defaultList(size: Int = 10): List<DepartmentRanking> {
            return List(size) { DEFAULT }
        }
    }
}
