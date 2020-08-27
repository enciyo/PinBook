package com.enciyo.pinbook.di

import com.enciyo.pinbook.data.PinBookCacheDataSource
import com.enciyo.pinbook.data.PinBookLocalDataSource
import com.enciyo.pinbook.data.PinBookRemoteDataSource
import com.enciyo.pinbook.data.PinBookRepositoryImp
import com.enciyo.pinbook.data.db.PinBookLocalDataSourceImp
import com.enciyo.pinbook.data.db.dao.BookCategoryDao
import com.enciyo.pinbook.data.db.dao.BookDao
import com.enciyo.pinbook.data.db.dao.BookFavoriteEntityDao
import com.enciyo.pinbook.data.db.dao.BookShowcaseDao
import com.enciyo.pinbook.data.remote.PinBookRemoteDataSourceImp
import com.enciyo.pinbook.data.remote.PinBookService
import com.enciyo.pinbook.domain.PinBookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class DataModule {


    @Provides
    @Singleton
    fun providePinBookRemoteDataSource(service: PinBookService) =
        PinBookRemoteDataSourceImp(service) as PinBookRemoteDataSource

    @Provides
    @Singleton
    fun providePinBookLocalDataSource(
        bookDao: BookDao,
        bookCategoryDao: BookCategoryDao,
        bookShowcaseDao: BookShowcaseDao,
        favoriteEntityDao: BookFavoriteEntityDao
    ) = PinBookLocalDataSourceImp(
        bookDao,
        bookCategoryDao,
        bookShowcaseDao,
        favoriteEntityDao
    ) as PinBookLocalDataSource


    @Provides
    @Singleton
    fun providePinBookRepository(
        remote: PinBookRemoteDataSource,
        local: PinBookLocalDataSource,
        pinBookCacheDataSource: PinBookCacheDataSource
    ) = PinBookRepositoryImp(
        remote,
        local,
        pinBookCacheDataSource
    ) as PinBookRepository


}