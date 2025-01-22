package com.teamhy2.designsystem.util.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController =
    compositionLocalOf<NavController> {
        throw Error("NavController가 제공되지 않았습니다.")
    }
