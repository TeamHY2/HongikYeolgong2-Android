package com.teamhy2.ranking

import java.time.LocalDate
import java.time.temporal.WeekFields

class WeekNumberCalculator(now: LocalDate = LocalDate.now()) {
    var currentWeekNumber: Int = calculateWeekNumber(now)
        private set
    val latestWeekNumber: Int = currentWeekNumber

    val isMinimumWeekNumber: Boolean
        get() = currentWeekNumber == MINIMUM_WEEK_NUMBER

    val isMaximumWeekNumber: Boolean
        get() = currentWeekNumber == latestWeekNumber

    /**
     * 주어진 날짜에 대해 weekNumber를 계산한다.
     *
     * weekNumber 기준:
     * - 주는 월요일~일요일 단위.
     * - 해당 달의 최소 4일 포함 되어야 하는 기준 ISO 방식을 따른다.
     * - 해당 주의 weekNumber는, 지정 연도(weekBasedYear)의 첫 주(ISO 기준)부터 몇 번째 주인지를 의미한다.
     *
     * 반환 형식: year * 100 + weekIndex
     * 예) 2025년 9번째 주 → 202509
     */
    private fun calculateWeekNumber(date: LocalDate): Int {
        val weekFields: WeekFields = WeekFields.ISO
        val weekOfYear: Int = date.get(weekFields.weekOfWeekBasedYear())
        val year: Int = date.get(weekFields.weekBasedYear())
        return year * 100 + weekOfYear
    }

    fun shiftWeekNumber(offset: Int) {
        val weekFields: WeekFields = WeekFields.ISO
        val year: Int = currentWeekNumber / 100
        val weekOfYear: Int = currentWeekNumber % 100

        val firstWeekMondayOfYear: LocalDate =
            LocalDate.of(year, 1, 1)
                .with(weekFields.dayOfWeek(), 1L)

        val currentWeekMonday: LocalDate =
            firstWeekMondayOfYear.plusWeeks((weekOfYear - 1).toLong())

        val newWeekMonday: LocalDate = currentWeekMonday.plusWeeks(offset.toLong())

        currentWeekNumber = calculateWeekNumber(newWeekMonday)
    }

    fun moveToPreviousWeek() {
        shiftWeekNumber(-1)
    }

    fun moveToNextWeek() {
        shiftWeekNumber(1)
    }

    companion object {
        private const val MINIMUM_WEEK_NUMBER = 202401
    }
}
