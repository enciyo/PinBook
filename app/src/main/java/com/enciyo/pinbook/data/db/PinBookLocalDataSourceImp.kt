package com.enciyo.pinbook.data.db

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.data.PinBookLocalDataSource
import com.enciyo.pinbook.data.db.dao.BookCategoryDao
import com.enciyo.pinbook.data.db.dao.BookDao
import com.enciyo.pinbook.data.db.dao.BookFavoriteEntityDao
import com.enciyo.pinbook.data.db.dao.BookShowcaseDao
import com.enciyo.pinbook.domain.mapper.toBook
import com.enciyo.pinbook.domain.mapper.toBookEntity
import com.enciyo.pinbook.domain.mapper.toFavoriteBook
import com.enciyo.pinbook.domain.mapper.toShowcaseBooks
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.PopularBooks
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
    private val mShowCaseDao: BookShowcaseDao,
    private val mFavoriteDao: BookFavoriteEntityDao
) : PinBookLocalDataSource {

    override fun getDBAllBooks(): Flow<List<Book>> = flow {
        mBookDao.getAllBooks()
            .map { it.toBook() }
            .also {
                emitAll(it)
            }
    }

    override suspend fun insertDBAllBooks(books: List<Book>) = withContext(Dispatchers.IO) {
        val fromDB = mBookDao.getBlockAllBooks()
        val fromApi = books.map { it.toBookEntity() }
        val diffList = fromApi.minus(fromDB)
        mBookDao.insertAll(diffList)
    }

    override suspend fun deleteDBAllBooks(books: List<Book>) {
        //mBookDao.deleteAll()
    }

    override suspend fun insertDBShowcaseBooks(books: List<ShowcaseBooks>) {
        val dbBook = books.map { it.toShowcaseBooks() }
        mShowCaseDao.insert(dbBook)
    }

    override suspend fun deleteDBShowcaseBooks(books: List<ShowcaseBooks>) =
        mShowCaseDao.deleteAll()

    override fun getDBAllShowcaseBooks(): Flow<List<ShowcaseBooks>> =
        mShowCaseDao.getAllShowcaseBooks()
            .map {
                it.toShowcaseBooks()
            }

    override fun getDBFavoriteBooks(): Flow<List<PopularBooks>> =
        mFavoriteDao.getAllBooks()
            .map {
                it.toFavoriteBook()
            }

    override suspend fun insertDBFavoriteBook(book: PopularBooks): PopularBooks? {
        mFavoriteDao.insert(book.toFavoriteBook())
        return book
    }

    override suspend fun deleteFavoriteBook(parameters: PopularBooks): AwesomeResult<Boolean> {
        mFavoriteDao.delete(parameters.toFavoriteBook())
        return AwesomeResult.Success(true, isApi = false)
    }


    override suspend fun getFavoriteBook(bookId: String): AwesomeResult<PopularBooks> {
        val book = with(Dispatchers.IO) {
            mFavoriteDao.getBook(bookId.toInt())
        }
        return if (book != null)
            AwesomeResult.Success(book.toBook(), false)
        else
            AwesomeResult.UnknownError(NullPointerException())
    }
}