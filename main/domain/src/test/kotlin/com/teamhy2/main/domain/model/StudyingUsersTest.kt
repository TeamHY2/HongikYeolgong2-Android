package com.teamhy2.main.domain.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class StudyingUsersTest : BehaviorSpec({
    Given("학습중인 유저들이 주어지고") {
        val mockStudyingUsers =
            listOf(
                StudyingUser(
                    userId = 7515,
                    userName = "Marietta Park",
                    studyDuration = "23:00:12",
                    studyStatus = true,
                ),
                StudyingUser(
                    userId = 5515,
                    userName = "JuYeong Park",
                    studyDuration = "01:00:12",
                    studyStatus = false,
                ),
                StudyingUser(
                    userId = 4515,
                    userName = "JinYeong Moon",
                    studyDuration = "11:02:12",
                    studyStatus = true,
                ),
            )

        val studyingUsers = StudyingUsers(mockStudyingUsers)

        When("현재 공부중인 유저 수를 가져오면") {
            val actual: Int = studyingUsers.studyingUsersCount

            Then("현재 학습 중인 유저는 2이다") {
                actual shouldBe 2
            }
        }
    }

    Given("학습중인 유저 2명 + 학습중이지 않은 유저 1명이 주어지고") {
        val mockStudyingUsers =
            listOf(
                StudyingUser(
                    userId = 7515,
                    userName = "Marietta Park",
                    studyDuration = "25:00:12",
                    studyStatus = true,
                ),
                StudyingUser(
                    userId = 5515,
                    userName = "JuYeong Park",
                    studyDuration = "01:00:12",
                    studyStatus = false,
                ),
                StudyingUser(
                    userId = 4515,
                    userName = "JinYeong Moon",
                    studyDuration = "11:02:59",
                    studyStatus = true,
                ),
                StudyingUser(
                    userId = 1235,
                    userName = "SangHyeon Moon",
                    studyDuration = "23:59:59",
                    studyStatus = true,
                ),
            )

        val studyingUsers = StudyingUsers(mockStudyingUsers)

        When("1초 뒤로 학습시간을 업데이트하면") {
            val actual: StudyingUsers = studyingUsers.updateStudyDurationsByOneSecond()

            Then("현재 학습 중인 유저들만 1초 뒤로 학습 시간이 업데이트 된다") {
                actual shouldBe
                    StudyingUsers(
                        listOf(
                            StudyingUser(
                                userId = 7515,
                                userName = "Marietta Park",
                                studyDuration = "25:00:13",
                                studyStatus = true,
                            ),
                            StudyingUser(
                                userId = 5515,
                                userName = "JuYeong Park",
                                studyDuration = "01:00:12",
                                studyStatus = false,
                            ),
                            StudyingUser(
                                userId = 4515,
                                userName = "JinYeong Moon",
                                studyDuration = "11:03:00",
                                studyStatus = true,
                            ),
                            StudyingUser(
                                userId = 1235,
                                userName = "SangHyeon Moon",
                                studyDuration = "24:00:00",
                                studyStatus = true,
                            ),
                        ),
                    )
            }
        }
    }

    Given("현재 학습중이지 않은 유저들이 주어지고") {
        val mockStudyingUsers =
            listOf(
                StudyingUser(
                    userId = 7515,
                    userName = "Marietta Park",
                    studyDuration = "23:00:12",
                    studyStatus = false,
                ),
                StudyingUser(
                    userId = 5515,
                    userName = "JuYeong Park",
                    studyDuration = "01:00:12",
                    studyStatus = false,
                ),
                StudyingUser(
                    userId = 4515,
                    userName = "JinYeong Moon",
                    studyDuration = "11:02:12",
                    studyStatus = false,
                ),
            )

        val studyingUsers = StudyingUsers(mockStudyingUsers)

        When("1초 뒤로 학습시간을 업데이트하면") {
            val actual: StudyingUsers = studyingUsers.updateStudyDurationsByOneSecond()

            Then("현재 학습 중인 유저는 없으므로 그대로 기존 인스턴스를 반환한다") {
                actual shouldBeSameInstanceAs studyingUsers
            }
        }
    }
})
