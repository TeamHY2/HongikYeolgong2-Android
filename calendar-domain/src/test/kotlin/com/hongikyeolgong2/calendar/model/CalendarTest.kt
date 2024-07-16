package com.hongikyeolgong2.calendar.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CalendarTest : BehaviorSpec({
    Given("LocalDate가 주어지면, 해당 달의 끝날짜를 알 수 있다.") {
        var date: LocalDate
        var calendar: Calendar
        var actual: Int

        When("1월이 주어지면") {
            date = LocalDate.of(2024, 1, 1)
            calendar = Calendar(date)
            actual = calendar.getLastDayOfMonth()

            Then("31을 반환한다") {
                actual shouldBe 31
            }
        }

        When("윤년이 아닌 2월달이 주어지면") {
            date = LocalDate.of(2023, 2, 1)
            calendar = Calendar(date)
            actual = calendar.getLastDayOfMonth()

            Then("28이 반환된다") {
                actual shouldBe 28
            }
        }

        When("윤년인 2월달이 주어지면") {
            date = LocalDate.of(2024, 2, 1)
            calendar = Calendar(date)
            actual = calendar.getLastDayOfMonth()

            Then("29가 반환된다") {
                actual shouldBe 29
            }
        }
    }
})
