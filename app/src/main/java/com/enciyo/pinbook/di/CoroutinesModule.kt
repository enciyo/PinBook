package com.enciyo.pinbook.di

import com.enciyo.pinbook.common.CoScope
import com.enciyo.pinbook.common.DefaultDispatcher
import com.enciyo.pinbook.common.IoDispatcher
import com.enciyo.pinbook.common.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object CoroutinesModule {

    @DefaultDispatcher
    @JvmStatic
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @JvmStatic
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @JvmStatic
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @CoScope
    @Provides
    fun provideCoroutineScope(@MainDispatcher coroutineDispatcher: CoroutineDispatcher) =
        CoroutineScope(coroutineDispatcher + Job())


}