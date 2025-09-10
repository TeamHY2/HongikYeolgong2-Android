package com.teamhy2.main.domain.model

import com.teamhy2.main.domain.util.formatSecondsToTime
import com.teamhy2.main.domain.util.parseTimeToSeconds
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StringTimeMapperTest() : BehaviorSpec({
    Given("시간 문자열을 총 초 단위로 바꿔주는 parseTimeToSeconds 함수에") {
        When("시간 문자열 '01:02:03'이 주어지면") {
            val timeString = "01:02:03"
            val expectedSeconds = 3723L // (1 * 3600) + (2 * 60) + 3

            Then("총 초 단위인 3723L를 반환해야 한다") {
                val result = parseTimeToSeconds(timeString)
                result shouldBe expectedSeconds
            }
        }

        When("시간 문자열 '00:00:00'이 주어지면") {
            val timeString = "00:00:00"
            val expectedSeconds = 0L

            Then("총 초단위인 0L을 반환해야 한다") {
                val result = parseTimeToSeconds(timeString)
                result shouldBe expectedSeconds
            }
        }

        When("시간, 분, 초 중 일부가 0인 시간 문자열 '10:00:05'이 주어지면") {
            val timeString = "10:00:05"
            val expectedSeconds = 36005L // (10 * 3600) + (0 * 60) + 5

            Then("총 초 단위인 36005L를 반환해야 한다") {
                val result = parseTimeToSeconds(timeString)
                result shouldBe expectedSeconds
            }
        }

        When("자리수가 다양한 시간 문자열 '100:1:12' 문자열이 주어지면") {
            val timeString = "100:1:12"
            val expectedSeconds = 360072L

            Then("총 초 단위인 359999L를 반환해야 한다") {
                val result = parseTimeToSeconds(timeString)
                result shouldBe expectedSeconds
            }
        }

        When("숫자가 아닌 문자('aa:bb:cc')가 포함된 문자열을 입력하면") {
            val invalidTimeString = "aa:bb:cc"

            Then("NumberFormatException 예외가 발생한다") {
                shouldThrow<NumberFormatException> {
                    parseTimeToSeconds(invalidTimeString)
                }
            }
        }

        When("':' 구분자가 부족하여 형식이 불완전한('10:20') 문자열을 입력하면") {
            val incompleteTimeString = "10:20"

            Then("IndexOutOfBoundsException 예외가 발생한다") {
                shouldThrow<IndexOutOfBoundsException> {
                    parseTimeToSeconds(incompleteTimeString)
                }
            }
        }

        When("문자열이 비어있는 경우") {
            val emptyTimeString = ""

            Then("NumberFormatException 예외가 발생한다") {
                shouldThrow<NumberFormatException> {
                    parseTimeToSeconds(emptyTimeString)
                }
            }
        }
    }

    Given("초를 'HH:mm:ss' 형식으로 변환하는 formatSecondsToTime 함수에") {
        When("0초가 입력되면") {
            val totalSeconds = 0L
            Then("'00:00:00' 문자열을 반환한다") {
                formatSecondsToTime(totalSeconds) shouldBe "00:00:00"
            }
        }

        When("3723초(1시간 2분 3초)가 입력되면") {
            val totalSeconds = 3723L
            Then("'01:02:03' 문자열을 반환한다") {
                formatSecondsToTime(totalSeconds) shouldBe "01:02:03"
            }
        }

        When("100시간을 초과하는 363661초(101시간 1분 1초)가 입력되면") {
            val totalSeconds = 363661L
            Then("'101:01:01' 문자열을 반환한다") {
                formatSecondsToTime(totalSeconds) shouldBe "101:01:01"
            }
        }

        When("음수 값(-1초)이 입력되면") {
            val negativeSeconds = -1L
            Then("IllegalArgumentException 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    formatSecondsToTime(negativeSeconds)
                }
            }
        }
    }
})
