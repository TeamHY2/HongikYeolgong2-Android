package com.teamhy2.main.data.di

import com.teamhy2.main.data.repository.DefaultWiseSayingRepository
import com.teamhy2.main.domain.WiseSayingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WiseSayingModule {
    @Binds
    @Singleton
    abstract fun bindWiseSayingRepository(impl: DefaultWiseSayingRepository): WiseSayingRepository
}
