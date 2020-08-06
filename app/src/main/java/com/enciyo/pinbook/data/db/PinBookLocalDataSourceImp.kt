package com.enciyo.pinbook.data.db

import com.enciyo.pinbook.data.PinBookLocalDataSource
import com.enciyo.pinbook.data.db.dao.BookCategoryDao
import com.enciyo.pinbook.data.db.dao.BookDao
import com.enciyo.pinbook.data.db.dao.BookShowcaseDao
import com.enciyo.pinbook.domain.mapper.toBook
import com.enciyo.pinbook.domain.mapper.toBookEntity
import com.enciyo.pinbook.domain.mapper.toShowcaseBooks
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PinBookLocalDataSourceImp @Inject constructor(
    private val mBookDao: BookDao,
    private val mCategoryDao: BookCategoryDao,
    private val mShowCaseDao: BookShowcaseDao
) : PinBookLocalDataSource {

  override fun getAllBooks(): Flow<List<Book>> = flow {
    emitAll(mBookDao.getAllBooks().map { bookEntities ->
      bookEntities.map { it.toBook() }
    })
  }

  override suspend fun insertAllBooks(books: List<Book>) = withContext(Dispatchers.IO) {
    val fromDB = mBookDao.getBlockAllBooks()
    val fromApi = books.map { it.toBookEntity() }
    val diffList = fromApi.minus(fromDB)
    mBookDao.insertAll(diffList)
  }

  override suspend fun deleteAllBooks(books: List<Book>) {
    //mBookDao.deleteAll()
  }

  override suspend fun insertShowcaseBooks(books: List<ShowcaseBooks>) {
    val fromDB = mShowCaseDao.getBlockAllShowcaseBooks()
    val fromApi = books.map { it.toShowcaseBooks() }
    val diffList = fromApi.minus(fromDB)
    mShowCaseDao.insert(diffList)
  }

  override suspend fun deleteShowcaseBooks(books: List<ShowcaseBooks>) {
    //mShowCaseDao.deleteAll()
  }

  override fun getAllShowcaseBooks(): Flow<List<ShowcaseBooks>> {
    return mShowCaseDao.getAllShowcaseBooks().map {
      it.map { it.toShowcaseBooks() }
    }
  }


}