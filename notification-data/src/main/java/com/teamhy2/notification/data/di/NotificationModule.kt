package com.teamhy2.notification.data.di

import com.teamhy2.notification.data.repository.RemoteNotificationRepository
import com.teamhy2.notification.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: RemoteNotificationRepository): NotificationRepository
}
