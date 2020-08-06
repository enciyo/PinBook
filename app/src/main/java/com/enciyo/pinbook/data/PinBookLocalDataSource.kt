package com.enciyo.pinbook.data

import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import kotlinx.coroutines.flow.Flow


interface PinBookLocalDataSource {

  fun getAllShowcaseBooks(): Flow<List<ShowcaseBooks>>
  fun getAllBooks(): Flow<List<Book>>

  suspend fun insertAllBooks(books: List<Book>)
  suspend fun insertShowcaseBooks(books: List<ShowcaseBooks>)


  suspend fun deleteAllBooks(books: List<Book>)
  suspend fun deleteShowcaseBooks(books: List<ShowcaseBooks>)


}