package com.teamhy2.onboarding.data.di

import com.teamhy2.onboarding.data.repository.DefaultWebViewRepository
import com.teamhy2.onboarding.data.repository.InMemoryDepartmentRepository
import com.teamhy2.onboarding.data.repository.fake.FakeUserRepository
import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import com.teamhy2.onboarding.domain.repository.UserRepository
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWebViewRepository(defaultWebViewRepository: DefaultWebViewRepository): WebViewRepository

    @Binds
    @Singleton
    abstract fun bindsNewUserRepository(fakeUserRepository: FakeUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindsDepartmentRepository(inMemoryDepartmentRepository: InMemoryDepartmentRepository): DepartmentRepository
}
