package com.teamhy2.main.domain.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    /**
     * Checks if the current date is within the specified date range.
     *
     * @param startDateString The start date string of the range.
     * @param endDateString The end date string of the range.
     * @return True if the current date is within the range, false otherwise.
     */
    fun isTodayWithinDateRange(
        startDateString: String,
        endDateString: String,
    ): Boolean {
        val formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy-M-d]")

        val startDateResult =
            runCatching {
                LocalDate.parse(startDateString, formatter)
            }

        val endDateResult =
            runCatching {
                LocalDate.parse(endDateString, formatter)
            }

        if (startDateResult.isFailure || endDateResult.isFailure) {
            return false
        }

        val startDate = startDateResult.getOrNull()
        val endDate = endDateResult.getOrNull()
        val todayDate: LocalDate = LocalDate.now()

        return todayDate.isEqual(startDate) || todayDate.isEqual(endDate) ||
            (todayDate.isAfter(startDate) && todayDate.isBefore(endDate))
    }
}
