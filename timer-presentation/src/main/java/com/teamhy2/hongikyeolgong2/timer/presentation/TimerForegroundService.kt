package com.teamhy2.hongikyeolgong2.timer.presentation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import com.teamhy2.main.domain.repository.StudyDayRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.ZoneId
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

        @Inject
        lateinit var studyDayRepository: StudyDayRepository

        private val serviceScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        private var timerJob: Job? = null

        private val startTimeState = MutableStateFlow(LocalDateTime.now())

        private var hasNotified30Min: Boolean = false
        private var hasNotified10Min: Boolean = false
        private var hasNotified0Min: Boolean = false

        override fun onBind(intent: Intent?): IBinder? = null

        override fun onStartCommand(
            intent: Intent?,
            flags: Int,
            startId: Int,
        ): Int {
            if (intent == null) return START_STICKY

            val startTimeMillis: Long =
                intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
            val endTimeMillis: Long =
                intent.getLongExtra(EXTRA_END_TIME, System.currentTimeMillis() + 1000 * 60 * 60 * 4)

            val startTime: LocalDateTime =
                LocalDateTime.ofInstant(
                    ofEpochMilli(startTimeMillis),
                    ZoneId.systemDefault(),
                )

            val endTime: LocalDateTime =
                LocalDateTime.ofInstant(
                    ofEpochMilli(endTimeMillis),
                    ZoneId.systemDefault(),
                )

            Log.d("bandal", "startTime: $startTime")
            Log.d("bandal", "endTime: $endTime")

            startForeground(
                TIMER_NOTIFICATION_ID,
                notificationHandler.buildServiceNotification(),
            )
            startTimer(
                startTime = startTime,
                endTime = endTime,
            )

            return START_NOT_STICKY
        }

        override fun onDestroy() {
            timerJob?.cancel()
            super.onDestroy()
        }

        private fun startTimer(
            startTime: LocalDateTime,
            endTime: LocalDateTime,
        ) {
            timerJob?.cancel()

            timerJob =
                serviceScope.launch {
                    while (true) {
                        val leftTime =
                            endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                        Log.d("bandal", "remainingTime: $leftTime")

                        if (leftTime <= 0 && hasNotified0Min.not()) {
                            studyDayRepository.saveStudyDay(
                                startDateTime = startTime,
                                endDateTime = endTime,
                            )
                            notificationHandler.showSimpleNotification(PushText.ZERO_MINUTES)
                            hasNotified0Min = true
                            stopSelf()
                            break
                        }

                        when {
                            leftTime <= 600000 && hasNotified10Min.not() -> {
                                notificationHandler.showSimpleNotification(PushText.TEN_MINUTES)
                                hasNotified10Min = true
                                continue
                            }

                            leftTime <= 1800000 && hasNotified30Min.not() -> {
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
            endDateTime: LocalDateTime,
        ) {
            val appContext: Context = context.applicationContext

            startTimeState.update { startDateTime }

            val startTimeMillis: Long =
                startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endTimeMillis: Long =
                endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val startIntent: Intent =
                Intent(appContext, TimerForegroundService::class.java).apply {
                    putExtra(EXTRA_START_TIME, startTimeMillis)
                    putExtra(EXTRA_END_TIME, endTimeMillis)
                }

            appContext.startForegroundService(startIntent)
        }

        override fun stopService() {
            val stopIntent = Intent(context, TimerForegroundService::class.java)
            runBlocking {
                withTimeout(4000L) {
                    studyDayRepository.saveStudyDay(startDateTime = startTimeState.value, endDateTime = LocalDateTime.now())
                }.onFailure {
                    Log.d("bandal", "stopService: ${it.message}")
                }
            }
            Log.d("bandal", "onDestroy: called ${startTimeState.value} and now ${LocalDateTime.now()}")
            context.stopService(stopIntent)
        }

        companion object {
            const val TIMER_NOTIFICATION_ID: Int = 1
            const val EXTRA_START_TIME: String = "extra_start_time"
            const val EXTRA_END_TIME: String = "extra_end_time"
        }
    }
