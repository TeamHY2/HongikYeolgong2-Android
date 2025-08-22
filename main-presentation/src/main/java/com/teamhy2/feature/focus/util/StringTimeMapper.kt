package com.teamhy2.feature.focus.util

import java.util.Locale
import java.util.concurrent.TimeUnit

fun parseTimeToSeconds(timeString: String): Long {
    val parts = timeString.split(':').map { it.toLong() }
    return TimeUnit.HOURS.toSeconds(parts[0]) + TimeUnit.MINUTES.toSeconds(parts[1]) + parts[2]
}

fun formatSecondsToTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.KOREA, "%d:%02d:%02d", hours, minutes, seconds)
}
