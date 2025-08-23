package com.teamhy2.main.data.di

import com.teamhy2.main.data.datasource.RemoteConfigPromotionDataSource
import com.teamhy2.main.data.repository.DefaultPromotionRepository
import com.teamhy2.main.data.repository.RemoteStudyRepository
import com.teamhy2.main.data.repository.RemoteWiseSayingRepository
import com.teamhy2.main.domain.datasource.PromotionDataSource
import com.teamhy2.main.domain.repository.PromotionRepository
import com.teamhy2.main.domain.repository.StudyRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindWiseSayingRepository(remoteWiseSayingRepository: RemoteWiseSayingRepository): WiseSayingRepository

    @Binds
    abstract fun bindPromotionDataSource(remoteConfigPromotionDataSource: RemoteConfigPromotionDataSource): PromotionDataSource

    @Binds
    @Singleton
    abstract fun bindPromotionRepository(defaultPromotionRepository: DefaultPromotionRepository): PromotionRepository

    @Binds
    @Singleton
    abstract fun bindRemoteStudyRepository(remoteStudyRepository: RemoteStudyRepository): StudyRepository
}
