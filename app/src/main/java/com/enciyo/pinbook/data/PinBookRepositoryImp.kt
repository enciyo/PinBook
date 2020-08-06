package com.enciyo.pinbook.data

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject


class PinBookRepositoryImp @Inject constructor(
    private val mRemote: PinBookRemoteDataSource,
    private val mLocal: PinBookLocalDataSource,
    private val mCache: PinBookCacheDataSource
) : PinBookRepository {


  override fun fetchLogin(username: String, password: String) = flow<AwesomeResult<User>> {
    when (val response = mRemote.fetchLogin(username, password)) {
      is AwesomeResult.Success -> {
        mCache.setCurrentUser(response.data)
        emit(AwesomeResult.Success(response.data, true))
      }
      else -> {
        emit(response)
      }
    }
  }

  override fun getCurrentUser(): Flow<AwesomeResult<User>> = flow {
    when (val user = mCache.currentUser()) {
      null -> AwesomeResult.UnknownError(NullPointerException())
      else -> AwesomeResult.Success(user, false)
    }
  }

  override fun getBooks(): Flow<AwesomeResult<List<Book>>> = DataManager.syncData(
      fromDB = mLocal::getAllBooks,
      fromApi = mRemote::fetchBooksAll,
      insert = mLocal::insertAllBooks,
      delete = mLocal::deleteAllBooks
  )

  override suspend fun checkServerIsAvailable(): AwesomeResult<ResponseBody> = mRemote.checkIsServerAvailable()


  override fun fetchShowcaseBooks(): Flow<AwesomeResult<List<ShowcaseBooks>>> = DataManager.syncData(
      fromDB = mLocal::getAllShowcaseBooks,
      fromApi = mRemote::fetchShowCaseBooks,
      insert = mLocal::insertShowcaseBooks,
      delete = mLocal::deleteShowcaseBooks
  )

}
