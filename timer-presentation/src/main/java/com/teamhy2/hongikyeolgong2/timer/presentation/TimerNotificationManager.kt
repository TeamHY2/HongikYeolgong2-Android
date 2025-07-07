package com.teamhy2.hongikyeolgong2.timer.presentation

import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlag

class TimerNotificationManager(
    private val notificationHandler: NotificationHandler,
) {
    fun showNotificationByLeftTime(notificationTimerFlag: NotificationTimeFlag) {
        when (notificationTimerFlag) {
            NotificationTimeFlag.FINISH_TIME ->
                notificationHandler.showSimpleNotification(PushText.ZERO_MINUTES)

            NotificationTimeFlag.TEN_MINUTES_MS ->
                notificationHandler.showSimpleNotification(PushText.TEN_MINUTES)

            NotificationTimeFlag.THIRTY_MINUTES_MS ->
                notificationHandler.showSimpleNotification(PushText.THIRTY_MINUTES)
        }
    }
}
