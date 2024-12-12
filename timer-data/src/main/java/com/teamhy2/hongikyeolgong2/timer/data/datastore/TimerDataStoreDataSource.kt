package com.teamhy2.hongikyeolgong2.timer.data.datastore

import android.util.Log
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
            Log.d("bandal", "getStartTime: $startTime")
            return startTime.toLocalDateTimeIso()
        }

        override suspend fun getEndTime(): LocalDateTime? {
            val endTime: String =
                dataStore.data.map { preferences ->
                    preferences[endTimeKey]
                }.firstOrNull() ?: return null
            Log.d("bandal", "getEndTime: $endTime")

            return endTime.toLocalDateTimeIso()
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

        override suspend fun clearTimes() {
            dataStore.edit { preferences ->
                preferences.remove(startTimeKey)
                preferences.remove(endTimeKey)
            }
        }

        private fun String.toLocalDateTimeIso(): LocalDateTime {
            return LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }

        companion object {
            private const val START_TIME_KEY = "start_time"
            private const val END_TIME_KEY = "end_time"

            private val startTimeKey = stringPreferencesKey(START_TIME_KEY)
            private val endTimeKey = stringPreferencesKey(END_TIME_KEY)
        }
    }
