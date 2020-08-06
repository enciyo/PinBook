package com.enciyo.pinbook.data.remote

import com.enciyo.pinbook.data.remote.model.ResponseBooks
import com.enciyo.pinbook.data.remote.model.ResponseCategories
import com.enciyo.pinbook.data.remote.model.ResponseUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface PinBookService {


  @GET("books/all")
  suspend fun fetchBooksAll(): Response<List<ResponseBooks>>

  @GET("books/search")
  suspend fun searchBooks(@Query("q") query: String): MutableList<ResponseBooks>


  @GET("books/filter")
  suspend fun filterBooks(@Query("categoryId") categoryID: String): MutableList<ResponseBooks>

  @GET("categories")
  suspend fun fetchCategories(): MutableList<ResponseCategories>

  @POST("rate")
  suspend fun rateBook(
      @Query("username") username: String,
      @Query("bookId") bookID: String,
      @Query("rating") rating: Double,
      @Query("comment") comment: String
  ): ResponseBody

  @GET("example")
  suspend fun checkServerIsAvailable() : Response<ResponseBody>

  @POST("register")
  suspend fun register(@Body responseUser: ResponseUser): ResponseUser

  @POST("login")
  suspend fun login(
      @Query("username") username: String,
      @Query("password") password: String
  ) : Response<ResponseUser>


}