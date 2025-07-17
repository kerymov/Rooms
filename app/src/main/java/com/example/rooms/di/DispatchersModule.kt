package com.example.rooms.di

import com.example.domain_core.utils.coroutines.DefaultDispatcher
import com.example.domain_core.utils.coroutines.IoDispatcher
import com.example.domain_core.utils.coroutines.MainDispatcher
import com.example.domain_core.utils.coroutines.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @MainDispatcher
    fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @MainImmediateDispatcher
    fun provideMainImmediateDispatcher() : CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @Singleton
    @MainDispatcher
    fun provideUnconfinedDispatcher() : CoroutineDispatcher = Dispatchers.Unconfined
}