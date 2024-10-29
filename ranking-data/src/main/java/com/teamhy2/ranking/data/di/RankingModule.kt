package com.teamhy2.ranking.data.di

import com.teamhy2.ranking.data.repository.RemoteRankingRepository
import com.teamhy2.ranking.repository.RankingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RankingModule {
    @Binds
    @Singleton
    abstract fun bindRankingRepository(remoteRankingRepository: RemoteRankingRepository): RankingRepository
}
