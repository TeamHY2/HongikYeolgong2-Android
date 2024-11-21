package com.teamhy2.designsystem.util.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import com.teamhy2.tracker.Tracker

val LocalTracker =
    compositionLocalOf<Tracker> {
        error("No Tracker provided")
    }
