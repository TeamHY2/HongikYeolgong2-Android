package com.teamhy2.hongikyeolgong2.timer.data.di

import com.teamhy2.hongikyeolgong2.timer.data.datastore.TimerDataStoreDataSource
import com.teamhy2.hongikyeolgong2.timer.data.repository.RemoteTimerRepository
import com.teamhy2.hongikyeolgong2.timer.model.TimerDataSource
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
    abstract fun bindTimerRepository(remoteTimerRepository: RemoteTimerRepository): TimerRepository

    @Binds
    @Singleton
    abstract fun bindTimerDataSource(timerDataStoreDataSource: TimerDataStoreDataSource): TimerDataSource
}
