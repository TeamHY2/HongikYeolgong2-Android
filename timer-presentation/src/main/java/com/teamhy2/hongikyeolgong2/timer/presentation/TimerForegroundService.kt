package com.teamhy2.hongikyeolgong2.timer.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlag
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlags
import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import com.teamhy2.main.domain.repository.StudyDayRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService
    @Inject
    constructor() : LifecycleService(), TimerService {
        @Inject
        @ApplicationContext
        lateinit var context: Context

        @Inject
        lateinit var notificationHandler: NotificationHandler

        private val timerNotificationManager: TimerNotificationManager
            by lazy { TimerNotificationManager(notificationHandler) }

        private val notificationTimeFlags = NotificationTimeFlags()

        @Inject
        lateinit var studyDayRepository: StudyDayRepository

        private var timerJob: Job? = null

        private lateinit var startTimeState: LocalDateTime

        override fun onStartCommand(
            intent: Intent?,
            flags: Int,
            startId: Int,
        ): Int {
            if (intent == null) return START_STICKY

            val startTimeMillis: Long =
                intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
            val endTimeMillis: Long =
                intent.getLongExtra(EXTRA_END_TIME, System.currentTimeMillis() + FOUR_HOURS_MILLIS)

            val startTime: LocalDateTime = startTimeMillis.toLocalDateTime()
            val endTime: LocalDateTime = endTimeMillis.toLocalDateTime()

            Log.i("TimerForegroundService", "startTime: $startTime / endTime: $endTime")

            startForeground(
                TIMER_NOTIFICATION_ID,
                notificationHandler.buildServiceNotification(),
            )
            startTimer(
                startTime = startTime,
                endTime = endTime,
            )

            return super.onStartCommand(intent, flags, startId)
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
                lifecycleScope.launch {
                    while (true) {
                        val leftTime = endTime.toEpochMillis() - LocalDateTime.now().toEpochMillis()

                        val currentReachedNotificationTimeFlag: NotificationTimeFlag? =
                            notificationTimeFlags.getFlagByLeftTimeMillis(leftTime)

                        currentReachedNotificationTimeFlag?.let {
                            timerNotificationManager.showNotificationByLeftTime(it)
                        }

                        if (currentReachedNotificationTimeFlag == NotificationTimeFlag.FINISH_TIME) {
                            saveStudyDayWhenTimerFinished(startTime, endTime)
                            stopSelf()
                            break
                        }
                        delay(ONE_SECOND)
                    }
                }
        }

        private suspend fun saveStudyDayWhenTimerFinished(
            startTime: LocalDateTime,
            endTime: LocalDateTime,
        ) {
            studyDayRepository.saveStudyDay(startTime, endTime)
                .onFailure {
                    // TODO: 서버 API 변경 후 처리 (로컬 DB로 기록 임시 저장)
                    Log.d(
                        "TimerForegroundService",
                        "checkAndNotifyIfTimeReached: ${it.message}",
                    )
                }
        }

        override fun startService(
            startDateTime: LocalDateTime,
            endDateTime: LocalDateTime,
        ) {
            val appContext: Context = context.applicationContext

            startTimeState = startDateTime

            val startTimeMillis: Long = startDateTime.toEpochMillis()
            val endTimeMillis: Long = endDateTime.toEpochMillis()

            val startIntent: Intent =
                Intent(appContext, TimerForegroundService::class.java).apply {
                    putExtra(EXTRA_START_TIME, startTimeMillis)
                    putExtra(EXTRA_END_TIME, endTimeMillis)
                }

            appContext.startForegroundService(startIntent)
        }

        override fun stopService() {
            lifecycleScope.launch {
                studyDayRepository.saveStudyDay(
                    startDateTime = startTimeState,
                    endDateTime = LocalDateTime.now(),
                )
                    .onFailure {
                        Log.d("TimerForegroundService", "stopService: ${it.message}")
                    }
                stopSelf()
            }
        }

        private fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(ofEpochMilli(this), ZoneId.systemDefault())

        private fun LocalDateTime.toEpochMillis(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        companion object {
            private const val ONE_SECOND = 1000L
            private const val FOUR_HOURS_MILLIS = ONE_SECOND * 60 * 60 * 4
            const val TIMER_NOTIFICATION_ID: Int = 1
            const val EXTRA_START_TIME: String = "extra_start_time"
            const val EXTRA_END_TIME: String = "extra_end_time"
        }
    }
