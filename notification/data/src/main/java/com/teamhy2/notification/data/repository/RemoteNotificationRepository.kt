package com.teamhy2.notification.data.repository

import com.benenfeldt.remote.api.NotificationService
import com.benenfeldt.remote.dto.NotificationResponse
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.notification.data.datastore.ReadNotificationDataStore
import com.teamhy2.notification.data.mapper.toDomain
import com.teamhy2.notification.domain.model.Notification
import com.teamhy2.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteNotificationRepository @Inject constructor(
    private val notificationService: NotificationService,
    private val readNotificationDataStore: ReadNotificationDataStore,
) : NotificationRepository {
    override suspend fun getNotifications(): Result<List<Notification>> {
        return notificationService.getNotifications().toResult { baseResponse ->
            baseResponse.data.map(NotificationResponse::toDomain)
        }
    }

    override suspend fun hasUnreadNotifications(): Result<Boolean> {
        return runCatching {
            val notifications = getNotifications().getOrThrow()
            val readNotificationIds = readNotificationDataStore.readNotificationIds.first()

            notifications.any { notification ->
                notification.notificationId !in readNotificationIds
            }
        }
    }

    override suspend fun markNotificationsAsRead(): Result<Unit> {
        return runCatching {
            val notifications = getNotifications().getOrThrow()
            val notificationIds = notifications.map { it.notificationId }.toSet()
            readNotificationDataStore.addReadNotificationIds(notificationIds)
        }
    }
}
