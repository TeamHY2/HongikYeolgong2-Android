package com.teamhy2.hongikyeolgong2.timer.presentation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService
    @Inject
    constructor() : Service(), TimerService {
        @Inject
        lateinit var notificationHandler: NotificationHandler

        @Inject
        @ApplicationContext
        lateinit var context: Context

        private val serviceScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        private var timerJob: Job? = null
        private var endTime: Long = 0L

        private var hasNotified30Min: Boolean = false
        private var hasNotified10Min: Boolean = false
        private var hasNotified0Min: Boolean = false

        override fun onBind(intent: Intent?): IBinder? = null

        override fun onStartCommand(
            intent: Intent?,
            flags: Int,
            startId: Int,
        ): Int {
            val startTimeMillis: Long =
                intent?.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
                    ?: System.currentTimeMillis()
            val durationMillis: Long = intent?.getLongExtra(EXTRA_TIME, 0L) ?: 0L

            val startTime: LocalDateTime =
                LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(startTimeMillis),
                    ZoneId.systemDefault(),
                )
            val duration: Duration = Duration.ofMillis(durationMillis)

            startForeground(
                TIMER_NOTIFICATION_ID,
                notificationHandler.buildServiceNotification(),
            )
            startTimer(startTime, duration)

            return START_NOT_STICKY
        }

        override fun onDestroy() {
            timerJob?.cancel()
            super.onDestroy()
        }

        private fun startTimer(
            startTime: LocalDateTime,
            duration: Duration,
        ) {
            timerJob?.cancel()

            val startTimeInMillis: Long =
                startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            endTime = startTimeInMillis + duration.toMillis()

            hasNotified30Min = false
            hasNotified10Min = false

            timerJob =
                serviceScope.launch {
                    while (true) {
                        val remainingTime: Long = endTime - System.currentTimeMillis()

                        if (remainingTime <= 0 && hasNotified0Min.not()) {
                            notificationHandler.showSimpleNotification(PushText.ZERO_MINUTES)
                            hasNotified0Min = true
                            stopSelf()
                            break
                        }

                        when {
                            remainingTime <= TimeUnit.MINUTES.toMillis(10L) && hasNotified10Min.not() -> {
                                notificationHandler.showSimpleNotification(PushText.TEN_MINUTES)
                                hasNotified10Min = true
                                continue
                            }

                            remainingTime <= TimeUnit.MINUTES.toMillis(30L) && hasNotified30Min.not() -> {
                                notificationHandler.showSimpleNotification(PushText.THIRTY_MINUTES)
                                hasNotified30Min = true
                                continue
                            }
                        }

                        delay(1000L)
                    }
                }
        }

        override fun startService(
            startDateTime: LocalDateTime,
            duration: Duration,
        ) {
            val appContext: Context = context.applicationContext
            val startTimeMillis: Long =
                startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val startIntent: Intent =
                Intent(appContext, TimerForegroundService::class.java).apply {
                    putExtra(EXTRA_START_TIME, startTimeMillis)
                    putExtra(EXTRA_TIME, duration.toMillis())
                }
            appContext.startForegroundService(startIntent)
        }

        override fun stopService() {
            val stopIntent = Intent(context, TimerForegroundService::class.java)
            context.stopService(stopIntent)
        }

        companion object {
            const val TIMER_NOTIFICATION_ID: Int = 1
            const val EXTRA_TIME: String = "extra_time"
            const val EXTRA_START_TIME: String = "extra_start_time"
        }
    }
