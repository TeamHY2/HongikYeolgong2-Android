package com.teamhy2.onboarding.data.di

import com.teamhy2.core.auth.GoogleSignIn
import com.teamhy2.core.auth.SocialSignIn
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class SocialSignInModule {
    @Binds
    @Singleton
    abstract fun bindsSocialSignIn(googleSignIn: GoogleSignIn): SocialSignIn
}
