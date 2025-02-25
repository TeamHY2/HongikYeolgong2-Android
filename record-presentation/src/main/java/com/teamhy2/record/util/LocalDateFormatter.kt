package com.teamhy2.record.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DATE_STATE_FORMAT = "MMMM d, yyyy"

fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern(DATE_STATE_FORMAT, Locale.ENGLISH)
    return this.format(formatter)
}
