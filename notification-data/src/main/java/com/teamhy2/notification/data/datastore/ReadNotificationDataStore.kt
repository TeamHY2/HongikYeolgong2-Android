package com.teamhy2.notification.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.teamhy2.notification.data.di.NotificationDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 읽은 알림 ID를 저장하는 DataStore
 * DataStore Preferences는 Set<Long>을 직접 지원하지 않으므로 Set<String>으로 저장하고 변환합니다.
 */
private val READ_NOTIFICATION_IDS_KEY = stringSetPreferencesKey("read_notification_ids")

@Singleton
class ReadNotificationDataStore
    @Inject
    constructor(
        @NotificationDataStore private val dataStore: DataStore<Preferences>,
    ) {
        /**
         * 읽은 알림 ID 목록을 Flow로 제공합니다.
         * 저장된 String ID를 Long으로 변환하며, 변환 실패한 항목은 무시됩니다.
         */
        val readNotificationIds: Flow<Set<Long>> =
            dataStore.data.map { preferences ->
                val stringIds = preferences[READ_NOTIFICATION_IDS_KEY] ?: emptySet()
                stringIds.mapNotNull { it.toLongOrNull() }.toSet()
            }

        /**
         * 단일 알림 ID를 읽은 것으로 표시합니다.
         */
        suspend fun addReadNotificationId(notificationId: Long) {
            dataStore.edit { preferences: MutablePreferences ->
                val currentStringIds = preferences[READ_NOTIFICATION_IDS_KEY] ?: emptySet()
                preferences[READ_NOTIFICATION_IDS_KEY] = currentStringIds + notificationId.toString()
            }
        }

        /**
         * 여러 알림 ID를 읽은 것으로 표시합니다.
         */
        suspend fun addReadNotificationIds(notificationIds: Set<Long>) {
            dataStore.edit { preferences: MutablePreferences ->
                val currentStringIds = preferences[READ_NOTIFICATION_IDS_KEY] ?: emptySet()
                val newStringIds = notificationIds.map { it.toString() }.toSet()
                preferences[READ_NOTIFICATION_IDS_KEY] = currentStringIds + newStringIds
            }
        }

        /**
         * 모든 읽은 알림 ID를 삭제합니다.
         */
        suspend fun clearReadNotificationIds() {
            dataStore.edit { preferences: MutablePreferences ->
                preferences.remove(READ_NOTIFICATION_IDS_KEY)
            }
        }
    }
