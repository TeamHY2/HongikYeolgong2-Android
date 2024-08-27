package com.teamhy2.hongikyeolgong2.timer.data.di

import com.teamhy2.hongikyeolgong2.timer.data.repository.DefaultTimerRepository
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimerModule {
    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: DefaultTimerRepository): TimerRepository
}
