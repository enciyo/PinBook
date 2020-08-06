package com.enciyo.pinbook.domain

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.domain.model.User
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody


interface PinBookRepository {


  fun fetchLogin(username:String,password:String) : Flow<AwesomeResult<User>>

  fun getBooks(): Flow<AwesomeResult<List<Book>>>

  fun fetchShowcaseBooks() : Flow<AwesomeResult<List<ShowcaseBooks>>>

  fun getCurrentUser() : Flow<AwesomeResult<User>>

  suspend fun checkServerIsAvailable() : AwesomeResult<ResponseBody>

}