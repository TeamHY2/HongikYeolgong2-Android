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

    Given("DateTimeFormatter 객체를 통해 표시형식을 정하여 현재 캘린더 상태를 알 수 있다") {
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

    Given("이전 달로 현재 달을 변경할 수 있다.") {
        var date: LocalDate
        var calendar: Calendar
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH)
        var actual: String

        When("2024년 1월 1일 LocalDate를 이전달로 변경하면") {
            date = LocalDate.of(2024, 1, 1)
            calendar = Calendar(date, dateTimeFormatter)
            calendar.moveToPreviousMonth()

            actual = calendar.now

            Then("2023-12-31을 반환한다.") {
                actual shouldBe "2023-12-01"
            }
        }

        When("2024년 2월 29일 LocalDate를 이전 달로 변경하면") {
            date = LocalDate.of(2024, 2, 29)
            calendar = Calendar(date, dateTimeFormatter)
            calendar.moveToPreviousMonth()

            actual = calendar.now

            Then("2024-01-29을 반환한다.") {
                actual shouldBe "2024-01-29"
            }
        }
    }

    Given("다음 달로 현재 달을 변경할 수 있다") {
        var date: LocalDate
        var calendar: Calendar
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH)
        var actual: String

        When("2024년 1월 1일 LocalDate를 다음 달로 변경하면") {
            date = LocalDate.of(2024, 1, 1)
            calendar = Calendar(date, dateTimeFormatter)
            calendar.moveToNextMonth()

            actual = calendar.now

            Then("2024-02-01을 반환한다.") {
                actual shouldBe "2024-02-01"
            }
        }

        When("2024년 1월 31일 LocalDate를 다음 달로 변경하면") {
            date = LocalDate.of(2024, 1, 31)
            calendar = Calendar(date, dateTimeFormatter)
            calendar.moveToNextMonth()

            actual = calendar.now

            Then("2024-02-29을 반환한다.") {
                actual shouldBe "2024-02-29"
            }
        }
    }

    Given("해당 달의 공부한 날들을 가져올 수 있다.") {
        var date: LocalDate
        var calendar: Calendar
        var studyDays: List<StudyDay>
        var actual: List<StudyDay>

        When("아무것도 없는 StudyDay List를 받았을 때") {
            date = LocalDate.of(2024, 2, 4)
            studyDays = emptyList()
            calendar = Calendar(
                initDate = date,
                studyDays = studyDays,
            )

            actual = calendar.getStudyDaysByMonth()

            Then("빈 List를 반환한다.") {
                actual shouldBe emptyList()
            }
        }

        When("모든 날짜가 있는 StudyDay List를 받았을 때") {
            date = LocalDate.of(2024, 2, 4)
            studyDays = listOf(
                StudyDay(LocalDate.of(2024, 1, 2), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                StudyDay(LocalDate.of(2024, 1, 5), StudyRoomUsage.USED_ONCE_EXTENDED_TWICE),
                StudyDay(LocalDate.of(2024, 2, 1), StudyRoomUsage.USED_ONCE),
                StudyDay(LocalDate.of(2024, 2, 3), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                StudyDay(LocalDate.of(2024, 2, 4), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
            )
            calendar = Calendar(
                initDate = date,
                studyDays = studyDays,
            )

            actual = calendar.getStudyDaysByMonth()

            Then("2024년 2월의 StudyDay List를 반환한다.") {
                actual shouldBe listOf(
                    StudyDay(LocalDate.of(2024, 2, 1), StudyRoomUsage.USED_ONCE),
                    StudyDay(LocalDate.of(2024, 2, 3), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                    StudyDay(LocalDate.of(2024, 2, 4), StudyRoomUsage.USED_ONCE_EXTENDED_ONCE),
                )
            }
        }
    }
})
