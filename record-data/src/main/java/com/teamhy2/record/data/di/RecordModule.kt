package com.teamhy2.record.data.di

import com.teamhy2.record.data.repository.RemoteCalendarStudyDayRepository
import com.teamhy2.record.data.repository.RemoteStudyDurationRepository
import com.teamhy2.record.domain.repository.CalendarStudyDayRepository
import com.teamhy2.record.domain.repository.StudyDurationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordModule {
    @Binds
    @Singleton
    abstract fun bindStudyDayDurationRepository(imp: RemoteStudyDurationRepository): StudyDurationRepository

    @Binds
    @Singleton
    abstract fun bindCalendarStudyDayRepository(imp: RemoteCalendarStudyDayRepository): CalendarStudyDayRepository
}
