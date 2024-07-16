package com.hongikyeolgong2.calendar.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    Given("DateTimeFormatter 객체로 표시형식을 현재 캘린더 상태를 알 수 있다") {
        var dateTimeFormatter: DateTimeFormatter
        var date: LocalDate
        var calendar: Calendar
        var actual: String

        When("MMM yyy 형식의 DateTimeFormatter 객체가 주어지면") {
            date = LocalDate.of(2024, 1, 1)
            dateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy").withLocale(Locale.ENGLISH)
            calendar = Calendar(date, dateTimeFormatter)
            actual = calendar.now

            Then("Jan 2024가 반환된다") {
                actual shouldBe "Jan 2024"
            }
        }

        When("yyyy-MM-dd 형식의 DateTimeFormatter 객체가 주어지면") {
            date = LocalDate.of(2024, 1, 1)
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH)
            calendar = Calendar(date, dateTimeFormatter)
            actual = calendar.now

            Then("2024-01-01이 반환된다") {
                actual shouldBe "2024-01-01"
            }
        }
    }
})
