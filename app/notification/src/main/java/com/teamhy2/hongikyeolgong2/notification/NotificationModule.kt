package com.teamhy2.hongikyeolgong2.notification

import android.content.Context
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun providesNotificationHandler(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope,
        settingsRepository: SettingsRepository,
    ): NotificationHandler {
        return NotificationHandler(
            context = context,
            coroutineScope = coroutineScope,
            settingsRepository = settingsRepository,
        )
    }
}
