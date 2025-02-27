package com.teamhy2.ranking

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class WeekNumberCalculatorTest : BehaviorSpec({
    Given("날짜(LocalDate)가 주어지면 weekNumber를 계산할 수 있다") {
        When("날짜가 2025년 1월 6일(월요일)인 경우") {
            val date: LocalDate = LocalDate.of(2025, 1, 6)
            val weekNumber: Int = WeekNumberCalculator.calculateWeekNumber(date)

            Then("주 번호는 202502가 되어야 한다") {
                weekNumber shouldBe 202502
            }
        }

        When("날짜가 2025년 12월 31일(수요일)인 경우") {
            val date: LocalDate = LocalDate.of(2025, 12, 31)
            val weekNumber: Int = WeekNumberCalculator.calculateWeekNumber(date)

            Then("주 번호는 202601이 되어야 한다") {
                weekNumber shouldBe 202601
            }
        }
    }

    Given("현재 weekNumber를 기반으로 다음주 or 저번주 weekNumber를 계산할 수 있다") {
        When("202502에서 +1 주 이동하면") {
            val shiftedWeek: Int = WeekNumberCalculator.shiftWeekNumber(202502, 1)

            Then("202503이 되어야 한다") {
                shiftedWeek shouldBe 202503
            }
        }

        When("202502에서 -1 주 이동하면") {
            val shiftedWeek: Int = WeekNumberCalculator.shiftWeekNumber(202502, -1)

            Then("202501이 되어야 한다") {
                shiftedWeek shouldBe 202501
            }
        }

        When("202501에서 -1 주 이동하면") {
            val shiftedWeek: Int = WeekNumberCalculator.shiftWeekNumber(202501, -1)

            Then("2024년의 마지막 주, 즉 202452가 되어야 한다") {
                shiftedWeek shouldBe 202452
            }
        }

        When("202452에서 +1 주 이동하면") {
            val shiftedWeek: Int = WeekNumberCalculator.shiftWeekNumber(202452, 1)

            Then("2025년의 첫 주, 즉 202501이 되어야 한다") {
                shiftedWeek shouldBe 202501
            }
        }
    }
})
