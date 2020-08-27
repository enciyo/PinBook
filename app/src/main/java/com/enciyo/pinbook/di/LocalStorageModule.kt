package com.enciyo.pinbook.di

import android.content.Context
import androidx.room.Room
import com.enciyo.pinbook.data.PinBookCacheDataSource
import com.enciyo.pinbook.data.cache.PinBookCacheDataSourceImp
import com.enciyo.pinbook.data.db.PinBookDatabase
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class LocalStorageModule {

  @Provides
  @Singleton
  fun providePinBookDatabase(@ApplicationContext context: Context) =
      Room.databaseBuilder(context, PinBookDatabase::class.java, "data-name")
          .fallbackToDestructiveMigration()
          .build()

  @Provides
  @Singleton
  fun provideBookDao(pinBookDatabase: PinBookDatabase) = pinBookDatabase.bookDao()

  @Provides
  @Singleton
  fun provideBookCategoryDao(pinBookDatabase: PinBookDatabase) = pinBookDatabase.bookCategoryDao()

  @Provides
  @Singleton
  fun provideShowcaseDao(pinBookDatabase: PinBookDatabase) = pinBookDatabase.bookShowcaseDao()

  @Provides
  @Singleton
  fun provideBookFavoriteEntity(pinBookDatabase: PinBookDatabase) = pinBookDatabase.bookFavoriteDao()


  @Provides
  @Singleton
  fun provideMMKVCache() = MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE,"encrpty")

  @Provides
  @Singleton
  fun provideAccountManagement(mmkv: MMKV) = PinBookCacheDataSourceImp(mmkv) as PinBookCacheDataSource


}