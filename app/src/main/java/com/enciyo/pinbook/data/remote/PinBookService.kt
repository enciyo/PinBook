package com.enciyo.pinbook.data.remote

import com.enciyo.pinbook.data.remote.model.ResponseBookDetail
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

    @GET("books/discovery")
    suspend fun fetchBookDiscovery(): Response<List<ResponseBooks>>


    @GET("books/popularbooks")
    suspend fun fetchPopularBooks(): Response<List<ResponseBooks>>


    @GET("books/search")
    suspend fun searchBooks(@Query("q") query: String): Response<List<ResponseBooks>>


    @GET("books/filter")
    suspend fun filterBooks(@Query("categoryId") categoryID: String): Response<List<ResponseBooks>>

    @GET("categories")
    suspend fun fetchCategories(): Response<MutableList<ResponseCategories>>

    @POST("detail")
    suspend fun bookDetail(
        @Query("id") bookID:Int
    ) : Response<ResponseBookDetail>

    @POST("rate")
    suspend fun rateBook(
        @Query("username") username: String,
        @Query("bookId") bookID: String,
        @Query("rating") rating: Double,
        @Query("comment") comment: String
    ): Response<ResponseBody>

    @GET("example")
    suspend fun checkServerIsAvailable(): Response<ResponseBody>

    @POST("register")
    suspend fun register(@Body responseUser: ResponseUser): Response<ResponseUser>

    @POST("login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<ResponseUser>


}