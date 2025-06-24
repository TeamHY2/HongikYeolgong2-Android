package com.teamhy2.hongikyeolgong2.timer.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.teamhy2.hongikyeolgong2.timer.model.TimerDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimerDataStoreDataSource
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : TimerDataSource {
        override suspend fun getStartTime(): LocalDateTime? {
            val startTime: String =
                dataStore.data.map { preferences ->
                    preferences[startTimeKey]
                }.firstOrNull() ?: return null
            return startTime.toLocalDateTimeIso()
        }

        override suspend fun getEndTime(): LocalDateTime? {
            val endTime: String =
                dataStore.data.map { preferences ->
                    preferences[endTimeKey]
                }.firstOrNull() ?: return null
            return endTime.toLocalDateTimeIso()
        }

        override suspend fun getCurrentStudySessionId(): Long? {
            val currentStudySessionId: String =
                dataStore.data.map { preferences ->
                    preferences[currentStudySessionIdKey]
                }.firstOrNull() ?: return null
            return currentStudySessionId.toLong()
        }

        override suspend fun saveStartTime(startTime: String) {
            dataStore.edit { preferences ->
                preferences[startTimeKey] = startTime
            }
        }

        override suspend fun saveEndTime(endTime: String) {
            dataStore.edit { preferences ->
                preferences[endTimeKey] = endTime
            }
        }

        override suspend fun saveCurrentStudySessionId(studySessionId: Long) {
            dataStore.edit { preferences ->
                preferences[currentStudySessionIdKey] = studySessionId.toString()
            }
        }

        override suspend fun clearTimes() {
            dataStore.edit { preferences ->
                preferences.remove(startTimeKey)
                preferences.remove(endTimeKey)
            }
        }

        override suspend fun clearCurrentStudySessionId() {
            dataStore.edit { preferences ->
                preferences.remove(currentStudySessionIdKey)
            }
        }

        private fun String.toLocalDateTimeIso(): LocalDateTime {
            return LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }

        companion object {
            private const val START_TIME_KEY = "start_time"
            private const val END_TIME_KEY = "end_time"
            private const val CURRENT_STUDY_SESSION_ID_KEY_NAME = "current_study_session_id"

            private val startTimeKey = stringPreferencesKey(START_TIME_KEY)
            private val endTimeKey = stringPreferencesKey(END_TIME_KEY)
            private val currentStudySessionIdKey = stringPreferencesKey(CURRENT_STUDY_SESSION_ID_KEY_NAME)
        }
    }
