package com.teamhy2.hongikyeolgong2.notification

import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class NotificationHandler
    @Inject
    constructor(
        private val context: Context,
        settingsRepository: SettingsRepository,
        coroutineScope: CoroutineScope,
    ) {
        private val notificationManager = context.getSystemService(NotificationManager::class.java)

        private val notificationChannel: StateFlow<Boolean> =
            settingsRepository.notificationSwitchState.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false,
            )

        init {
            coroutineScope.launch {
                notificationChannel.collect()
            }
        }

        fun showSimpleNotification(pushText: PushText) {
            val notification =
                NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(context.getString(pushText.id))
                    .setSmallIcon(R.drawable.ic_status_bar_logo)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setAutoCancel(true)
                    .build()

            if (notificationChannel.value) {
                notificationManager.notify(Random.nextInt(), notification)
            }
        }

        companion object {
            private const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
        }
    }

enum class PushText(
    @StringRes val id: Int,
) {
    THIRTY_MINUTES(R.string.notification_content_thirty_minutes_remain),
    TEN_MINUTES(R.string.notification_content_ten_minutes_remain),
}
