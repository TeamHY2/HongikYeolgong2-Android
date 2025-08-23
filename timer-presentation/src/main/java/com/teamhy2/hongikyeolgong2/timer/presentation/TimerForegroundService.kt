package com.teamhy2.hongikyeolgong2.timer.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlag
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlags
import com.teamhy2.main.domain.repository.StudyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService : LifecycleService() {
    @Inject
    lateinit var notificationHandler: NotificationHandler

    private val timerNotificationManager: TimerNotificationManager
        by lazy { TimerNotificationManager(notificationHandler) }

    private val notificationTimeFlags = NotificationTimeFlags()

    @Inject
    lateinit var studyRepository: StudyRepository

    private var timerJob: Job? = null

    private val studySessionId: MutableStateFlow<Long> = MutableStateFlow(-1L)

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        if (intent?.action == null) return START_STICKY

        when (intent.action) {
            TimerServiceManager.ACTION_START -> {
                val startTimeMillis: Long =
                    intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
                val endTimeMillis: Long =
                    intent.getLongExtra(
                        EXTRA_END_TIME,
                        System.currentTimeMillis() + FOUR_HOURS_MILLIS,
                    )

                startService(startTimeMillis, endTimeMillis)
            }

            TimerServiceManager.ACTION_STOP -> {
                stopService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService(
        startTimeMillis: Long,
        endTimeMillis: Long,
    ) {
        val startTime: LocalDateTime = startTimeMillis.toLocalDateTime()
        val endTime: LocalDateTime = endTimeMillis.toLocalDateTime()

        Log.i("TimerForegroundService", "startTime: $startTime | endTime: $endTime")

        startForeground(
            TIMER_NOTIFICATION_ID,
            notificationHandler.buildServiceNotification(),
        )
        startTimer(
            startTime = startTime,
            endTime = endTime,
        )
    }

    private fun startTimer(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        lifecycleScope.launch {
            studyRepository.startStudy(
                startTime = startTime.toString(),
            )
                .onSuccess { studyStartResult ->
                    studySessionId.update { studyStartResult.studySessionId }
                }
        }

        initTimerJob(endTime)
    }

    private fun initTimerJob(endTime: LocalDateTime) {
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
                        stopService()
                        break
                    }
                    delay(ONE_SECOND)
                }
            }
    }

    private fun stopService() {
        if (studySessionId.value == -1L) return

        lifecycleScope.launch {
            studyRepository.endStudy(
                studySessionId = studySessionId.value,
                endTime = LocalDateTime.now().toString(),
            )
                .onSuccess {
                    studySessionId.update { -1L }
                }
                .onFailure {
                    Log.d("TimerForegroundService", "stopService: ${it.message}")
                }
            stopSelf()
        }
    }

    override fun onDestroy() {
        timerJob?.cancel()
        timerJob = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
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
