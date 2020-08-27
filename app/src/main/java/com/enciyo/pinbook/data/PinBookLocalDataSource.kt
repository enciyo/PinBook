package com.enciyo.pinbook.data

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import kotlinx.coroutines.flow.Flow


interface PinBookLocalDataSource {

    fun getDBAllShowcaseBooks(): Flow<List<ShowcaseBooks>>
    fun getDBAllBooks(): Flow<List<Book>>
    fun getDBFavoriteBooks(): Flow<List<PopularBooks>>

    suspend fun insertDBAllBooks(books: List<Book>)
    suspend fun insertDBFavoriteBook(book: PopularBooks): PopularBooks?
    suspend fun insertDBShowcaseBooks(books: List<ShowcaseBooks>)


    suspend fun deleteDBAllBooks(books: List<Book>)
    suspend fun deleteDBShowcaseBooks(books: List<ShowcaseBooks>)
    suspend fun deleteFavoriteBook(parameters: PopularBooks): AwesomeResult<Boolean>
    suspend fun getFavoriteBook(s: String): AwesomeResult<PopularBooks>


}