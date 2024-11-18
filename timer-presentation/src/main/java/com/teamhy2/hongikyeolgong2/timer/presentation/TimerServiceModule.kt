package com.teamhy2.hongikyeolgong2.timer.presentation

import com.teamhy2.hongikyeolgong2.timer.model.TimerService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimerServiceModule {
    @Binds
    @Singleton
    abstract fun bindTimerService(timerForegroundService: TimerForegroundService): TimerService
}
