package com.teamhy2.main.data.di

import com.teamhy2.main.data.repository.DefaultHomeContentRepository
import com.teamhy2.main.domain.HomeContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeContentModule {
    @Binds
    @Singleton
    abstract fun bindHomeContentRepository(impl: DefaultHomeContentRepository): HomeContentRepository
}
