package com.teamhy2.designsystem.util.compositionlocal

import androidx.compose.runtime.compositionLocalOf

val LocalShowToast = compositionLocalOf<ShowToast> { error("No ShowToast provided") }

fun interface ShowToast {
    fun showToast(message: String)
}
