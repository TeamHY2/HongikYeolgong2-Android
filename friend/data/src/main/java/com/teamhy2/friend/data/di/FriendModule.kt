package com.teamhy2.friend.data.di

import com.teamhy2.friend.data.repository.FriendNotificationRepositoryImpl
import com.teamhy2.friend.data.repository.RemoteFriendRepository
import com.teamhy2.friend.domain.repository.FriendNotificationRepository
import com.teamhy2.friend.domain.repository.FriendRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FriendModule {
    @Binds
    @Singleton
    abstract fun bindFriendRepository(impl: RemoteFriendRepository): FriendRepository

    @Binds
    @Singleton
    abstract fun bindFriendNotificationRepository(impl: FriendNotificationRepositoryImpl): FriendNotificationRepository
}
