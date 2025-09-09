package com.teamhy2.hongikyeolgong2.timer.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlag
import com.teamhy2.hongikyeolgong2.timer.model.NotificationTimeFlags
import com.teamhy2.main.domain.repository.StudyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
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

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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

            TimerServiceManager.ACTION_EXTEND -> {
                val newEndTimeMillis: Long =
                    intent.getLongExtra(
                        EXTRA_NEW_END_TIME,
                        System.currentTimeMillis() + FOUR_HOURS_MILLIS,
                    )
                extendStudy(newEndTimeMillis)
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
        Log.i(TAG, "$startTime, EndTime: $endTime")
        startForeground(TIMER_NOTIFICATION_ID, notificationHandler.buildServiceNotification())
        startTimer(startTime = startTime, endTime = endTime)
    }

    private fun startTimer(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        serviceScope.launch {
            studyRepository.startStudy(startTime = startTime.toString())
                .onSuccess { studyStartResult ->
                    studySessionId.update { studyStartResult.studySessionId }
                    Log.d(TAG, "startStudy Success. Session ID: ${studyStartResult.studySessionId}")
                    initTimerJob(endTime)
                }
                .onFailure {
                    Log.d(TAG, "startStudy Fail: ${it.message}")
                    stopSelf()
                }
        }
    }

    private suspend fun initTimerJob(endTime: LocalDateTime) {
        timerJob?.cancelAndJoin()
        notificationTimeFlags.reset()
        timerJob =
            serviceScope.launch {
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

    private fun extendStudy(newEndTimeMillis: Long) {
        Log.d(TAG, "extendStudy called.")
        if (studySessionId.value == -1L) {
            Log.w(TAG, "Cannot extend study, no active session.")
            return
        }

        serviceScope.launch {
            timerJob?.cancelAndJoin()
            studyRepository.endStudy(
                studySessionId = studySessionId.value,
                endTime = LocalDateTime.now().toString(),
            )
                .onSuccess {
                    Log.d(TAG, "endStudy for extension successful.")
                    val newStartTime = LocalDateTime.now()
                    val newEndTime = newEndTimeMillis.toLocalDateTime()
                    startTimer(startTime = newStartTime, endTime = newEndTime)
                }
                .onFailure {
                    Log.e(TAG, "endStudy for extension failed: ${it.message}")
                    stopSelf()
                }
        }
    }

    private fun stopService() {
        if (studySessionId.value == -1L) {
            stopSelf()
            return
        }
        timerJob?.cancel()

        serviceScope.launch {
            studyRepository.endStudy(
                studySessionId = studySessionId.value,
                endTime = LocalDateTime.now().toString(),
            )
                .onSuccess {
                    Log.d(TAG, "stopService: endStudy successful.")
                    studySessionId.update { -1L }
                }
                .onFailure {
                    Log.e(TAG, "stopService: endStudy failed: ${it.message}")
                }
                .also {
                    stopSelf()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called. Cancelling serviceScope.")
        serviceScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(ofEpochMilli(this), ZoneId.systemDefault())

    private fun LocalDateTime.toEpochMillis(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    companion object {
        private const val TAG = "TimerForegroundService"
        private const val ONE_SECOND = 1000L
        private const val FOUR_HOURS_MILLIS = ONE_SECOND * 60 * 60 * 4
        const val TIMER_NOTIFICATION_ID: Int = 1
        const val EXTRA_START_TIME: String = "extra_start_time"
        const val EXTRA_END_TIME: String = "extra_end_time"
        const val EXTRA_NEW_END_TIME: String = "extra_new_end_time"
    }
}
