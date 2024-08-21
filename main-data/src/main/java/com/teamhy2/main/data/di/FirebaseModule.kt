package com.teamhy2.main.data.di

import com.teamhy2.main.data.repository.DefaultFirebaseRepository
import com.teamhy2.main.domain.FirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(impl: DefaultFirebaseRepository): FirebaseRepository
}
