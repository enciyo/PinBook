package com.enciyo.pinbook.data

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.domain.model.User
import okhttp3.ResponseBody


interface PinBookRemoteDataSource {


  suspend fun fetchLogin(username:String,password:String) : AwesomeResult<User>
  suspend fun fetchShowCaseBooks() : AwesomeResult<List<ShowcaseBooks>>
  suspend fun checkIsServerAvailable() : AwesomeResult<ResponseBody>
  suspend fun fetchBooksAll(): AwesomeResult<List<Book>>
  

}