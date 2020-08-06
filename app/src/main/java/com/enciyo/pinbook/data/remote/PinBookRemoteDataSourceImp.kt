package com.enciyo.pinbook.data.remote

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.map
import com.enciyo.pinbook.common.mapAllList
import com.enciyo.pinbook.data.DataManager
import com.enciyo.pinbook.data.PinBookRemoteDataSource
import com.enciyo.pinbook.domain.mapper.toBook
import com.enciyo.pinbook.domain.mapper.toShowcaseBook
import com.enciyo.pinbook.domain.mapper.toUser
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.domain.model.User
import okhttp3.ResponseBody
import javax.inject.Inject


class PinBookRemoteDataSourceImp @Inject constructor(
    private val mService: PinBookService
) : PinBookRemoteDataSource {


  override suspend fun fetchLogin(username: String, password: String): AwesomeResult<User> =
      DataManager
          .fetch { mService.login(username, password) }
          .map {
            it.toUser()
          }

  override suspend fun fetchBooksAll(): AwesomeResult<List<Book>> =
      DataManager
          .fetch { mService.fetchBooksAll() }
          .mapAllList {
            it.toBook()
          }

  override suspend fun checkIsServerAvailable(): AwesomeResult<ResponseBody> =
      DataManager
          .fetch { mService.checkServerIsAvailable() }

  override suspend fun fetchShowCaseBooks(): AwesomeResult<List<ShowcaseBooks>> =
      DataManager
          .fetch { mService.fetchBooksAll() }
          .map {
            it.shuffled()
                .take(5)
                .map {
                  it.toShowcaseBook()
                }
          }


}

