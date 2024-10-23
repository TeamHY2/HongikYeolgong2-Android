package com.teamhy2.feature.main

import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.ic_home
import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.ic_ranking
import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.ic_record
import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.ic_setting

enum class MainTab(
    val iconResId: Int,
    val contentDescription: String,
    val route: String,
) {
    HOME(
        iconResId = ic_home,
        contentDescription = "홈",
        route = "main",
    ),
    RECORD(
        iconResId = ic_record,
        contentDescription = "기록",
        route = "record",
    ),
    RANKING(
        iconResId = ic_ranking,
        contentDescription = "랭킹",
        route = "ranking",
    ),
    SETTING(
        iconResId = ic_setting,
        contentDescription = "설정",
        route = "setting",
    ),
    ;

    companion object {
        fun fromRoute(route: String?): MainTab =
            when (route) {
                HOME.route -> HOME
                RECORD.route -> RECORD
                RANKING.route -> RANKING
                SETTING.route -> SETTING
                else -> HOME
            }
    }
}
