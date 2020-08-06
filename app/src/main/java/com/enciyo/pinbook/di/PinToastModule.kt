package com.enciyo.pinbook.di

import android.content.Context
import com.enciyo.pinbook.common.PinToast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class PinToastModule {


  @Provides
  @Singleton
  fun provideApplicationToast(@ApplicationContext context:Context) : PinToast = PinToast(context)

}