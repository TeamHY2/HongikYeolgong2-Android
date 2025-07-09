package com.teamhy2.hongikyeolgong2.notification

import android.app.Notification
import androidx.annotation.StringRes

interface NotificationHandler {
    fun buildServiceNotification(): Notification

    fun notifyGeneralNotification(
        @StringRes stringRes: Int,
    )
}
