package com.teamhy2.feature.main

import androidx.annotation.DrawableRes
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_home
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_home_selected
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_ranking
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_ranking_selected
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_record
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_record_selected
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_setting
import com.teamhy2.hongikyeolgong2.main.presentation.R.drawable.ic_setting_selected

enum class MainTab(
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int,
    val contentDescription: String,
    val route: String,
) {
    HOME(
        iconResId = ic_home,
        selectedIconResId = ic_home_selected,
        contentDescription = "홈",
        route = "home",
    ),
    RECORD(
        iconResId = ic_record,
        selectedIconResId = ic_record_selected,
        contentDescription = "기록",
        route = "record",
    ),
    RANKING(
        iconResId = ic_ranking,
        selectedIconResId = ic_ranking_selected,
        contentDescription = "랭킹",
        route = "ranking",
    ),
    SETTING(
        iconResId = ic_setting,
        selectedIconResId = ic_setting_selected,
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
