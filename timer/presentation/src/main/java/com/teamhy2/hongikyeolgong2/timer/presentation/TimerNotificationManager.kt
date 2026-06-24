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
                notificationHandler.notifyGeneralNotification(PushText.ZERO_MINUTES.id)

            NotificationTimeFlag.TEN_MINUTES ->
                notificationHandler.notifyGeneralNotification(PushText.TEN_MINUTES.id)

            NotificationTimeFlag.THIRTY_MINUTES ->
                notificationHandler.notifyGeneralNotification(PushText.THIRTY_MINUTES.id)
        }
    }
}
