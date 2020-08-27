package com.enciyo.pinbook.data.remote

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.map
import com.enciyo.pinbook.common.mapAllList
import com.enciyo.pinbook.data.DataManager
import com.enciyo.pinbook.data.PinBookRemoteDataSource
import com.enciyo.pinbook.data.remote.model.ResponseUser
import com.enciyo.pinbook.domain.mapper.*
import com.enciyo.pinbook.domain.model.*
import okhttp3.ResponseBody
import javax.inject.Inject


class PinBookRemoteDataSourceImp @Inject constructor(
    private val mService: PinBookService
) : PinBookRemoteDataSource {


    override suspend fun fetchApiLogin(username: String, password: String): AwesomeResult<User> =
        DataManager
            .fetch { mService.login(username, password) }
            .map {
                it.toUser()
            }

    override suspend fun fetchApiBooksAll(): AwesomeResult<List<Book>> =
        DataManager
            .fetch { mService.fetchBooksAll() }
            .mapAllList {
                it.toBook()
            }


    override suspend fun fetchPopularBooks(): AwesomeResult<List<Book>> =
        DataManager
            .fetch { mService.fetchPopularBooks() }
            .mapAllList {
                it.toBook()
            }


    override suspend fun fetchApiCheckIsServerAvailable(): AwesomeResult<ResponseBody> =
        DataManager
            .fetch { mService.checkServerIsAvailable() }

    override suspend fun fetchDiscoveryBooks(): AwesomeResult<List<ShowcaseBooks>> =
        DataManager
            .fetch { mService.fetchBookDiscovery() }
            .map {
                it.shuffled()
                    .take(5)
                    .map {
                        it.toShowcaseBook()
                    }
            }

    override suspend fun fetchBooksCategories(): AwesomeResult<List<Category>> =
        DataManager
            .fetch { mService.fetchCategories() }
            .mapAllList {
                it.toCategory()
            }


    override suspend fun fetchFilteredBooks(categoryId: String?): AwesomeResult<List<Book>> =
        DataManager
            .fetch { mService.filterBooks(categoryId ?: "") }
            .mapAllList {
                it.toBook()
            }


    override suspend fun fetchBookDetail(bookID: Int): AwesomeResult<BookDetail> =
        DataManager
            .fetch {
                mService.bookDetail(bookID)
            }
            .map {
                it.toBookDetail()
            }

    override suspend fun rateBook(
        username: String,
        bookID: String,
        rating: Double,
        comment: String
    ): AwesomeResult<ResponseBody> =
        DataManager
            .fetch {
                mService.rateBook(username, bookID, rating, comment)
            }


    override suspend fun fetchRegister(
        username: String,
        password: String,
        displayName: String,
        email: String
    ): AwesomeResult<User> =
        DataManager
            .fetch {
                mService.register(
                    ResponseUser(
                        null,
                        displayName,
                        email,
                        password,
                        username
                    )
                )
            }
            .map {
                User(
                    username = it.mUsername,
                    displayName = it.mDisplayName,
                    mail = it.mEmail,
                    password = it.mPassword
                )
            }
}

