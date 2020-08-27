package com.enciyo.pinbook.domain

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody


interface PinBookRepository {

    fun fetchShowcaseBooks(): Flow<AwesomeResult<List<ShowcaseBooks>>>
    fun fetchLogin(username: String, password: String): Flow<AwesomeResult<User>>


    fun fetchBooks(): Flow<AwesomeResult<List<Book>>>
    fun fetchFavoriteBooks(): Flow<AwesomeResult.Success<List<PopularBooks>>>
    fun fetchPopularBooks(): Flow<AwesomeResult<List<Book>>>
    fun fetchFilteredBooks(query: String?): Flow<AwesomeResult<List<Book>>>


    suspend fun fetchCheckServerIsAvailable(): AwesomeResult<ResponseBody>
    suspend fun fetchCurrentUser(): AwesomeResult<User>
    suspend fun insertFavoriteBooks(book: PopularBooks): AwesomeResult<PopularBooks>
    suspend fun fetchCategories(): AwesomeResult<List<Category>>
    suspend fun fetchBookDetail(bookID: Int): AwesomeResult<BookDetail>
    suspend fun fetchRate(bookID: Int, rating: Double, comment: String): AwesomeResult<ResponseBody>
    suspend fun fetchRegister(
        username: String,
        password: String,
        displayName: String,
        email: String
    ): AwesomeResult<User>

    suspend fun deleteFavorite(parameters: PopularBooks): AwesomeResult<Boolean>
    suspend fun getFavoriteBook(parameters: String?): AwesomeResult<PopularBooks>

}