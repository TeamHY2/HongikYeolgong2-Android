package com.teamhy2.hongikyeolgong2.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HongikYeolgong2Application : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationChannel =
            NotificationChannel(
                "notification_channel_id",
                "Notification name",
                NotificationManager.IMPORTANCE_HIGH,
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)
    }
}
