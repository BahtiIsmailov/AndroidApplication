package com.example.englishapp.di

import com.example.englishapp.domain.UserInteractor
import com.example.englishapp.domain.UserInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DomainModule {

    @Binds
    abstract fun bindUserInteractor(userInteractor: UserInteractorImpl): UserInteractor
}