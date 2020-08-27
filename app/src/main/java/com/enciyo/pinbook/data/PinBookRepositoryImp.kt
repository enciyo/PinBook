package com.enciyo.pinbook.data

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import javax.inject.Inject


@ExperimentalCoroutinesApi
class PinBookRepositoryImp @Inject constructor(
    private val mRemote: PinBookRemoteDataSource,
    private val mLocal: PinBookLocalDataSource,
    private val mCache: PinBookCacheDataSource
) : PinBookRepository {




    override fun fetchLogin(username: String, password: String) = flow<AwesomeResult<User>> {
        val loginResponse = mRemote.fetchApiLogin(username, password)
        if (loginResponse is AwesomeResult.Success)
            mCache.setCurrentUser(loginResponse.data)
        emit(loginResponse)
    }

    override suspend fun fetchCurrentUser(): AwesomeResult<User> {
        val currentUser = mCache.currentUser()
        return if (currentUser != null)
            AwesomeResult.Success(currentUser, true)
        else
            AwesomeResult.UnknownError(NullPointerException())
    }


    override suspend fun fetchBookDetail(bookID: Int): AwesomeResult<BookDetail> = mRemote.fetchBookDetail(bookID)

    override fun fetchPopularBooks(): Flow<AwesomeResult<List<Book>>> = flow {
        emit(mRemote.fetchPopularBooks())
    }


    override suspend fun fetchCategories(): AwesomeResult<List<Category>> =
        mRemote.fetchBooksCategories()


    override fun fetchBooks(): Flow<AwesomeResult<List<Book>>> =
        DataManager.syncData(
            fromDB = mLocal::getDBAllBooks,
            fromApi = mRemote::fetchApiBooksAll,
            insert = mLocal::insertDBAllBooks,
            delete = mLocal::deleteDBAllBooks
        )

    override fun fetchShowcaseBooks(): Flow<AwesomeResult<List<ShowcaseBooks>>> =
        DataManager.fetchDataFromDbIfNoNetworkConnection(
            fromDB = mLocal::getDBAllShowcaseBooks,
            fromApi = mRemote::fetchDiscoveryBooks,
            insert = mLocal::insertDBShowcaseBooks,
            delete = mLocal::deleteDBShowcaseBooks
        )


    override suspend fun fetchRate(
        bookID: Int,
        rating: Double,
        comment: String
    ): AwesomeResult<ResponseBody> =
        mRemote.rateBook(mCache.currentUser()?.username.toString(),bookID.toString(),rating,comment)

    override suspend fun fetchCheckServerIsAvailable(): AwesomeResult<ResponseBody> =
        mRemote.fetchApiCheckIsServerAvailable()

    override fun fetchFavoriteBooks() =
        mLocal.getDBFavoriteBooks()
            .map {
                AwesomeResult.Success(it, false)
            }

    override suspend fun insertFavoriteBooks(book: PopularBooks): AwesomeResult<PopularBooks> {
        val bookInserted = mLocal.insertDBFavoriteBook(book)
        return if (bookInserted != null)
            AwesomeResult.Success(bookInserted, false)
        else
            AwesomeResult.UnknownError(Throwable())
    }

    override fun fetchFilteredBooks(query: String?): Flow<AwesomeResult<List<Book>>> = flow {
        emit(mRemote.fetchFilteredBooks(query))
    }


    override suspend fun deleteFavorite(parameters: PopularBooks): AwesomeResult<Boolean> {
        return mLocal.deleteFavoriteBook(parameters)
    }

    override suspend fun fetchRegister(
        username: String,
        password: String,
        displayName: String,
        email: String
    ): AwesomeResult<User> =
        mRemote.fetchRegister(username,password,displayName,email)


    override suspend fun getFavoriteBook(parameters: String?): AwesomeResult<PopularBooks> = mLocal
        .getFavoriteBook(parameters?:"")
}




