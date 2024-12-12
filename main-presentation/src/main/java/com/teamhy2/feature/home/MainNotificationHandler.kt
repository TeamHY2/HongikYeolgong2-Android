package com.teamhy2.feature.home

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.teamhy2.feature.main.MainActivity
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.notification.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

        private val notificationChannel: StateFlow<Boolean> =
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
            coroutineScope.launch {
                notificationChannel.collect()
            }
        }

        override fun buildServiceNotification(): Notification {
            Log.d("bandal", "buildServiceNotification: 호출")
            return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("홍익열공이 열공중")
                .setContentText("지금 열람실을 이용중이에요!")
                .setSmallIcon(R.drawable.ic_status_bar_logo)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }

        override fun buildGeneralNotification(contentText: String): Notification {
            Log.d("bandal", "buildGeneralNotification: 호출")

            return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("홍익열공이 알림")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_status_bar_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }

        override fun showSimpleNotification(pushText: PushText) {
            Log.d("bandal", "showSimpleNotification: $pushText")

            val notification = buildGeneralNotification(context.getString(pushText.id))

            if (notificationChannel.value) {
                notificationManager.notify(Random.nextInt(), notification)
            }
        }

        companion object {
            private const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
        }
    }
