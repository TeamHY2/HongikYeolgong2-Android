package com.teamhy2.notification.domain.repository

import com.teamhy2.notification.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>>
}
