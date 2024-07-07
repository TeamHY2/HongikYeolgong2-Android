package com.hongikyeolgong2

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class KotestTest: BehaviorSpec({
	Given("테스트를 위한 1과 2를") {
		val one = 1
		val two = 2
		When("더하면") {
			Then("3이 되어야 한다.") {
				one + two shouldBe 3
			}
		}
	}
})
