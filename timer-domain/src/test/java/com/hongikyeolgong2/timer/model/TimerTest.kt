package com.hongikyeolgong2.timer.model

import com.teamhy2.hongikyeolgong2.timer.model.Timer
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import java.time.Duration
import java.time.LocalTime

class TimerTest : BehaviorSpec({

    Given("Timer가 endTime을 계산할 수 있다.") {
        var startTime: LocalTime
        var duration: Duration
        var timer: Timer
        var actualEndTime: LocalTime

        When("startTime과 duration이 주어지면") {
            startTime = LocalTime.of(11, 30)
            duration = Duration.ofHours(6)
            timer =
                Timer(
                    startTime,
                    duration,
                    mapOf(
                        Timer.THIRTY_MINUTES_SECONDS to {},
                        Timer.TEN_MINUTES_SECONDS to {},
                        Timer.TIME_OVER_SECONDS to {},
                    ),
                )

            Then("startTime에 duration이 더해진 endTime이 반환된다.") {
                actualEndTime = timer.endTime
                actualEndTime shouldBe LocalTime.of(17, 30)
            }
        }
    }

    Given("Timer가 포맷팅된 남은 시간을 반환할 수 있다.") {
        var startTime: LocalTime
        var duration: Duration
        var timer: Timer

        When("11:30에 타이머가 시작되면 ") {
            startTime = LocalTime.of(11, 30)
            duration = Duration.ofSeconds(3600) // 01:00:00
            timer =
                Timer(
                    startTime,
                    duration,
                    mapOf(
                        Timer.THIRTY_MINUTES_SECONDS to {},
                        Timer.TEN_MINUTES_SECONDS to {},
                        Timer.TIME_OVER_SECONDS to {},
                    ),
                )

            Then("포맷팅된 남은 시간이 반환된다.") {
                timer.formattedLeftTime shouldBe "01:00:00"
            }
        }
    }

    Given("Timer가 남은시간을 emit 할 수 있다.") {
        var startTime: LocalTime
        var duration: Duration
        var timer: Timer

        When("11:30에 타이머가 시작되고 10초를 재면") {
            startTime = LocalTime.of(11, 30)
            duration = Duration.ofSeconds(10)
            timer =
                Timer(
                    startTime,
                    duration,
                    mapOf(
                        Timer.THIRTY_MINUTES_SECONDS to {},
                        Timer.TEN_MINUTES_SECONDS to {},
                        Timer.TIME_OVER_SECONDS to {},
                    ),
                )

            Then("Timer 이벤트가 10번 발생한다.") {
                runTest {
                    val eventsTimes = timer.emitTimerEvents().count()
                    eventsTimes shouldBe 10
                }
            }
        }
    }

    Given("Timer가 매초마다 남은시간을 감소할 수 있다.") {
        var startTime: LocalTime
        var duration: Duration
        var timer: Timer

        When("11:30에 타이머가 시작되고 10초를 재면") {
            startTime = LocalTime.of(11, 30)
            duration = Duration.ofSeconds(10)
            timer =
                Timer(
                    startTime,
                    duration,
                    mapOf(
                        Timer.THIRTY_MINUTES_SECONDS to {},
                        Timer.TEN_MINUTES_SECONDS to {},
                        Timer.TIME_OVER_SECONDS to {},
                    ),
                )

            Then("1초뒤 남은 시간 9초를 반환 한다.") {
                runTest {
                    timer.emitTimerEvents().first()
                    timer.leftTime.seconds shouldBe 9L
                }
            }
        }
    }
})
