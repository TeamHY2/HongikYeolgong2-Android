package com.teamhy2.ranking

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class WeekNumberCalculatorTest : BehaviorSpec({
    Given("날짜가 2025년 1월 6일(월요일)인 경우") {
        val date: LocalDate = LocalDate.of(2025, 1, 6)

        When("현재 주 번호를 계산하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val actualWeekNumber: Int = weekNumberCalculator.currentWeekNumber

            Then("주 번호는 202502가 되어야 한다") {
                actualWeekNumber shouldBe 202502
            }
        }
    }

    Given("날짜가 2025년 12월 31일(수요일)인 경우") {
        val date: LocalDate = LocalDate.of(2025, 12, 31)

        When("현재 주 번호를 계산하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val actualWeekNumber: Int = weekNumberCalculator.currentWeekNumber

            Then("주 번호는 202601이 되어야 한다") {
                actualWeekNumber shouldBe 202601
            }
        }
    }

    Given("날짜가 2025년 1월 6일로 주 번호가 202502이고") {
        val date: LocalDate = LocalDate.of(2025, 1, 6)

        When("다음 주로 이동하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val initWeekNumber = weekNumberCalculator.currentWeekNumber
            weekNumberCalculator.moveToNextWeek()
            val actualCurrentWeekNumber = weekNumberCalculator.currentWeekNumber

            Then("현재 주 번호는 202503이다") {
                initWeekNumber shouldBe 202502
                actualCurrentWeekNumber shouldBe 202503
            }
        }

        When("이전 주로 이동하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val initWeekNumber = weekNumberCalculator.currentWeekNumber
            weekNumberCalculator.moveToPreviousWeek()
            val actualCurrentWeekNumber = weekNumberCalculator.currentWeekNumber

            Then("현재 주 번호는 202501이다") {
                initWeekNumber shouldBe 202502
                actualCurrentWeekNumber shouldBe 202501
            }
        }
    }

    Given("날짜가 2025년 1월 1일로 주 번호가 202501이고") {
        val date: LocalDate = LocalDate.of(2025, 1, 1)

        When("이전 주로 이동하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val initWeekNumber = weekNumberCalculator.currentWeekNumber
            weekNumberCalculator.moveToPreviousWeek()
            val actualCurrentWeekNumber = weekNumberCalculator.currentWeekNumber

            Then("현재 주 번호는 202452이다.") {
                initWeekNumber shouldBe 202501
                actualCurrentWeekNumber shouldBe 202452
            }
        }
    }

    Given("날짜가 2024년 12월 29일(일)로 주 번호가 202452이고") {
        val date: LocalDate = LocalDate.of(2024, 12, 29)

        When("다음 주로 이동하면") {
            val weekNumberCalculator = WeekNumberCalculator(date)
            val initWeekNumber = weekNumberCalculator.currentWeekNumber
            weekNumberCalculator.moveToNextWeek()
            val actualCurrentWeekNumber = weekNumberCalculator.currentWeekNumber

            Then("현재 주 번호는 202501이다.") {
                initWeekNumber shouldBe 202452
                actualCurrentWeekNumber shouldBe 202501
            }
        }
    }

    Given("날짜가 2024년 1월 1일로 주 번호가 202401이고") {
        val date: LocalDate = LocalDate.of(2024, 1, 1)
        val weekNumberCalculator = WeekNumberCalculator(date)

        When("현재 최소 주 번호인지 확인하면") {
            val actual = weekNumberCalculator.isMinimumWeekNumber

            Then("true이다") {
                actual shouldBe true
            }
        }
    }

    Given("날짜가 2024년 1월 8일로 주 번호가 202402이고") {
        val date: LocalDate = LocalDate.of(2024, 1, 8)
        val weekNumberCalculator = WeekNumberCalculator(date)

        When("현재 최소 주 번호인지 확인하면") {
            val actual = weekNumberCalculator.isMinimumWeekNumber

            Then("false이다") {
                actual shouldBe false
            }
        }
    }

    Given("현재 날짜가 주어지고") {
        val date: LocalDate = LocalDate.now()
        val weekNumberCalculator = WeekNumberCalculator(date)

        When("현재 주 번호가 최대 주 번호인지 확인하면") {
            val actual = weekNumberCalculator.isMaximumWeekNumber

            Then("true이다") {
                actual shouldBe true
            }
        }
    }

    Given("현재 날짜에서 이전 주로 이동하고") {
        val date: LocalDate = LocalDate.now()
        val weekNumberCalculator = WeekNumberCalculator(date)
        weekNumberCalculator.moveToPreviousWeek()

        When("현재 주 번호가 최대 주 번호인지 확인하면") {
            val actual = weekNumberCalculator.isMaximumWeekNumber

            Then("true이다") {
                actual shouldBe false
            }
        }
    }
})
