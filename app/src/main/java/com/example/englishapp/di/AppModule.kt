package com.example.englishapp.di

import android.content.Context
import android.util.Log
import com.example.englishapp.model.datasource.IoDispatcher
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.LetMeSpeakRepositoryImpl
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.utils.ProfileRepositoryLocal
import com.example.englishapp.utils.TokenHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideTokenHelper(@ApplicationContext context: Context): TokenHelper {
        return TokenHelper(context)
    }

    @Singleton
    @Provides
    fun provideProfileRepositoryLocal(@ApplicationContext context: Context): ProfileRepositoryLocal {
        return ProfileRepositoryLocal(context)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(tokenHelper: TokenHelper): RemoteDataSource {
        val newToken = tokenHelper.generateDeviceToken()
        Log.d("token_device", newToken)
        return RemoteDataSource(newToken, tokenHelper)

    }

    @Singleton
    @Provides
    fun provideLetMeSpeakRepository(
        remoteDataSource: RemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): LetMeSpeakRepository {
        return LetMeSpeakRepositoryImpl(remoteDataSource, dispatcher)
    }

}