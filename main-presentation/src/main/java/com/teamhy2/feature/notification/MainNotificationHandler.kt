package com.teamhy2.feature.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.teamhy2.feature.main.MainActivity
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.notification.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.random.Random

class MainNotificationHandler
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        settingsRepository: SettingsRepository,
        coroutineScope: CoroutineScope,
    ) : NotificationHandler {
        private val notificationManager = context.getSystemService(NotificationManager::class.java)

        private val notificationSwitchState: StateFlow<Boolean> =
            settingsRepository.notificationSwitchState
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = false,
                )

        private val mainIntent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        private val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        init {
            initNotificationChannels()
        }

        private fun initNotificationChannels() {
            val serviceChannel =
                NotificationChannel(
                    SERVICE_NOTIFICATION_CHANNEL_ID,
                    "홍익열공이 열람실 이용중 알림",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "열람실 이용중임을 표시하는 알림입니다."
                }

            val generalChannel =
                NotificationChannel(
                    GENERAL_NOTIFICATION_CHANNEL_ID,
                    "홍익열공이 열람실 연장 요청 알림",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "열람실 연장 시간에 맞춰 알림을 표시합니다."
                }

            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(generalChannel)
        }

        override fun buildServiceNotification(): Notification {
            return NotificationCompat.Builder(context, SERVICE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("홍익열공이 열공중")
                .setContentText("지금 열람실을 이용중이에요!")
                .setSmallIcon(R.drawable.ic_status_bar_logo)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build()
        }

        override fun notifyGeneralNotification(
            @StringRes stringRes: Int,
        ) {
            val notification =
                NotificationCompat.Builder(context, GENERAL_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("홍익열공이 알림")
                    .setContentText(context.getString(stringRes))
                    .setSmallIcon(R.drawable.ic_status_bar_logo)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

            if (notificationSwitchState.value) {
                notificationManager.notify(Random.nextInt(), notification)
            }
        }

        companion object {
            private const val SERVICE_NOTIFICATION_CHANNEL_ID = "service_notification_channel_id"
            private const val GENERAL_NOTIFICATION_CHANNEL_ID = "general_notification_channel_id"
        }
    }
