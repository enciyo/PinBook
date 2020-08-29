package com.enciyo.pinbook.di

import android.content.Context
import com.enciyo.pinbook.common.network.NetworkConnectivityManager
import com.enciyo.pinbook.common.network.NetworkConnectivityManagerImp
import com.enciyo.pinbook.utils.resourcewrapper.ResourceWrapper
import com.enciyo.pinbook.utils.resourcewrapper.ResourceWrapperImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideNetworkConnectivityManager(@ApplicationContext context: Context): NetworkConnectivityManager =
        NetworkConnectivityManagerImp(context)

    @Singleton
    @Provides
    fun provideResourceWrapper(@ApplicationContext context: Context): ResourceWrapper = ResourceWrapperImp(context)

}