package com.teamhy2.designsystem.util.compositionlocal

import androidx.compose.runtime.compositionLocalOf

val LocalShowSnackBar = compositionLocalOf(defaultFactory = { ShowSnackBar {} })

fun interface ShowSnackBar {
    fun showSnackBar(message: String?)
}
