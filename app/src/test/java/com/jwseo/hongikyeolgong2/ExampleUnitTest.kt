package com.jwseo.hongikyeolgong2

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
	@Test
	fun addition_isCorrect() {
		assertEquals(4, 2 + 2)
	}

	@Test
	fun `Ktlint 테스트용 잘못된 유닛 테스트`() {
		assertEquals(3, 2 + 2)
	}
}
