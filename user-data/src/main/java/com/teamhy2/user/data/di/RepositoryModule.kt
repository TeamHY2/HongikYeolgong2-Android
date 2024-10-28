package com.teamhy2.user.data.di

import com.teamhy2.user.data.repository.RemoteUserRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsUserRepository(remoteUserRepository: RemoteUserRepository): UserRepository
}
