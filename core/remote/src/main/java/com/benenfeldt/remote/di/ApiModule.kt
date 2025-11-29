package com.benenfeldt.remote.di

import com.benenfeldt.remote.api.FriendService
import com.benenfeldt.remote.api.LibraryService
import com.benenfeldt.remote.api.StudyService
import com.benenfeldt.remote.api.TokenService
import com.benenfeldt.remote.api.UserPublicService
import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.api.WeeklyService
import com.benenfeldt.remote.api.WiseSayingService
import com.benenfeldt.remote.token.NeedAuthRetrofit
import com.benenfeldt.remote.token.PublicRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideUserService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserPublicService(
        @PublicRetrofit retrofit: Retrofit,
    ): UserPublicService {
        return retrofit.create(UserPublicService::class.java)
    }

    @Singleton
    @Provides
    fun provideWiseSayingService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): WiseSayingService {
        return retrofit.create(WiseSayingService::class.java)
    }

    @Singleton
    @Provides
    fun provideStudyService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): StudyService {
        return retrofit.create(StudyService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeeklyService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): WeeklyService {
        return retrofit.create(WeeklyService::class.java)
    }

    @Singleton
    @Provides
    fun provideTokenService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): TokenService {
        return retrofit.create(TokenService::class.java)
    }

    @Singleton
    @Provides
    fun provideLibraryService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): LibraryService {
        return retrofit.create(LibraryService::class.java)
    }

    @Singleton
    @Provides
    fun provideFriendService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): FriendService {
        return retrofit.create(FriendService::class.java)
    }
}
