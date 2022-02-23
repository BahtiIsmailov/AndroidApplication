package com.example.englishapp.di

import com.example.englishapp.domain.quantum.QuantumManager
import com.example.englishapp.domain.quantum.QuantumManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class QuantumModule {

    @Binds
    abstract fun bindQuantumManager(quantumManagerImpl: QuantumManagerImpl): QuantumManager

}