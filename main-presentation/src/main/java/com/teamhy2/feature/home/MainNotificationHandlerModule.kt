package com.teamhy2.feature.home

import com.teamhy2.hongikyeolgong2.notification.NotificationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainNotificationHandlerModule {
    @Binds
    @Singleton
    abstract fun bindMainNotificationHandler(mainNotificationHandler: MainNotificationHandler): NotificationHandler
}
