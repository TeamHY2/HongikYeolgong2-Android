package com.teamhy2.main.domain.util

import java.time.LocalDate

object DateUtil {
    /**
     * Checks if the current date is within the specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return True if the current date is within the range, false otherwise.
     */
    fun isTodayWithinDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        val today: LocalDate = LocalDate.now()
        return today.isEqual(startDate) || today.isEqual(endDate) ||
            (today.isAfter(startDate) && today.isBefore(endDate))
    }
}
