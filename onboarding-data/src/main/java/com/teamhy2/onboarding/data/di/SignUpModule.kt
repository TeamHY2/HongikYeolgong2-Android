package com.teamhy2.onboarding.data.di

import com.teamhy2.onboarding.data.repository.DefaultUserRepository
import com.teamhy2.onboarding.data.repository.InMemoryDepartmentRepository
import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import com.teamhy2.onboarding.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SignUpModule {
    @Binds
    @Singleton
    abstract fun bindsDepartmentRepository(inMemoryDepartmentRepository: InMemoryDepartmentRepository): DepartmentRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository
}
