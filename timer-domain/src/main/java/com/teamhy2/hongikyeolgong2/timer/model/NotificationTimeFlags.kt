package com.teamhy2.hongikyeolgong2.timer.model

class NotificationTimeFlags {
    private val notificationTimerFlags: MutableMap<NotificationTimeFlag, Boolean> =
        NotificationTimeFlag
            .entries
            .associate { it to false }
            .toMutableMap()

    operator fun get(notificationTimeFlag: NotificationTimeFlag): Boolean {
        return notificationTimerFlags[notificationTimeFlag]
            ?: throw IllegalArgumentException("유효하지 않은 NotificationTimeFlag: $notificationTimeFlag")
    }

    private operator fun set(
        notificationTimeFlag: NotificationTimeFlag,
        value: Boolean,
    ) {
        notificationTimerFlags[notificationTimeFlag] = value
    }

    fun getFlagByLeftTimeMillis(leftTimeMillis: Long): NotificationTimeFlag? {
        notificationTimerFlags.forEach { (notificationTimeFlag, _) ->
            if (leftTimeMillis <= notificationTimeFlag.millis && this[notificationTimeFlag].not()) {
                this[notificationTimeFlag] = true
                return notificationTimeFlag
            }
        }
        return null
    }
}
