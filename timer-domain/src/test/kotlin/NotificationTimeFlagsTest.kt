import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlag
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlags
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class NotificationTimeFlagsTest : ShouldSpec({
    should("모든 NotificationTimeFlag가 초기값으로 설정된다") {
        // given & when
        val notificationTimeFlags = NotificationTimeFlags()

        // then
        NotificationTimeFlag.entries.forEach { notificationTimeFlag ->
            notificationTimeFlags[notificationTimeFlag] shouldBe false
        }
    }

    should("인자로 받은 남은시간(밀리세컨드) 값이 NotificationTimeFlag의 모든 Millis 값보다 크면 null을 반환한다") {
        // given
        val notificationTimeFlags = NotificationTimeFlags()

        // when & then
        val biggerThanMaxOfNotificationTimeFlagMillis: Long =
            NotificationTimeFlag.entries.maxOf { it.millis } + 1
        notificationTimeFlags.getFlagByLeftTimeMillis(biggerThanMaxOfNotificationTimeFlagMillis) shouldBe null
    }

    should("인자로 받은 남은시간(밀리세컨드) 값에 따라 NotificationTimeFlag가 true로 변한다") {
        // given
        val notificationTimeFlags = NotificationTimeFlags()

        // when & then
        NotificationTimeFlag.entries.forEach { notificationTimeFlag ->
            val actual = notificationTimeFlags.getFlagByLeftTimeMillis(notificationTimeFlag.millis)
            actual shouldBe notificationTimeFlag
            notificationTimeFlags[notificationTimeFlag] shouldBe true
        }
    }

    should("인자로 받은 남은시간(밀리세컨드) 값보다 작지만 이미 플래그를 true로 설정했다면 null을 반환한다") {
        // given
        val notificationTimeFlags = NotificationTimeFlags()
        val actualNotificationTimeFlag =
            notificationTimeFlags.getFlagByLeftTimeMillis(NotificationTimeFlag.THIRTY_MINUTES.millis)
        actualNotificationTimeFlag shouldBe NotificationTimeFlag.THIRTY_MINUTES

        // when & then
        notificationTimeFlags.getFlagByLeftTimeMillis(NotificationTimeFlag.THIRTY_MINUTES.millis - 1) shouldBe null
    }

    should("인자로 받은 남은시간(밀리세컨드) 값이 0보다 작고 이미 플래그를 true로 설정했다면 null을 반환한다") {
        // given
        val notificationTimeFlags = NotificationTimeFlags()

        // 이전시간 플래그 true 처리
        notificationTimeFlags.getFlagByLeftTimeMillis(NotificationTimeFlag.THIRTY_MINUTES.millis)
        notificationTimeFlags.getFlagByLeftTimeMillis(NotificationTimeFlag.TEN_MINUTES.millis)
        val actualNotificationTimeFlag = notificationTimeFlags.getFlagByLeftTimeMillis(0)
        actualNotificationTimeFlag shouldBe NotificationTimeFlag.FINISH_TIME

        // when & then
        notificationTimeFlags.getFlagByLeftTimeMillis(-1) shouldBe null
    }
})
