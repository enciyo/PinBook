package com.enciyo.pinbook.data

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.*
import okhttp3.ResponseBody


interface PinBookRemoteDataSource {

    suspend fun fetchApiLogin(username: String, password: String): AwesomeResult<User>
    suspend fun fetchDiscoveryBooks(): AwesomeResult<List<ShowcaseBooks>>
    suspend fun fetchPopularBooks(): AwesomeResult<List<Book>>
    suspend fun fetchApiCheckIsServerAvailable(): AwesomeResult<ResponseBody>
    suspend fun fetchApiBooksAll(): AwesomeResult<List<Book>>
    suspend fun fetchBooksCategories(): AwesomeResult<List<Category>>
    suspend fun fetchFilteredBooks(categoryId: String?): AwesomeResult<List<Book>>
    suspend fun fetchBookDetail(bookID: Int): AwesomeResult<BookDetail>

    suspend fun rateBook(
        username: String,
        bookID: String,
        rating: Double,
        comment: String
    ): AwesomeResult<ResponseBody>

    suspend fun fetchRegister(
        username: String,
        password: String,
        displayName: String,
        email: String
    ): AwesomeResult<User>
}