package com.teamhy2.hongikyeolgong2.notification

import android.app.Notification

interface NotificationHandler {
    fun buildServiceNotification(): Notification

    fun buildGeneralNotification(contentText: String): Notification

    fun showSimpleNotification(pushText: PushText)
}
