package com.teamhy2.notification.data.repository

import com.benenfeldt.remote.api.NotificationService
import com.benenfeldt.remote.dto.NotificationResponse
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.notification.data.mapper.toDomain
import com.teamhy2.notification.domain.model.Notification
import com.teamhy2.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class RemoteNotificationRepository
    @Inject
    constructor(
        private val notificationService: NotificationService,
    ) : NotificationRepository {
        override suspend fun getNotifications(): Result<List<Notification>> {
            return notificationService.getNotifications().toResult { baseResponse ->
                baseResponse.data.map(NotificationResponse::toDomain)
            }
        }
    }
