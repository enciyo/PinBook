package com.enciyo.pinbook.di

import com.enciyo.pinbook.common.network.NetworkConnectivityManager
import com.enciyo.pinbook.data.remote.PinBookService
import com.enciyo.pinbook.data.remote.interceptors.NetworkConnectionInterceptor
import com.enciyo.pinbook.utils.UrlConstants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {


  @Provides
  @Singleton
  fun provideNetworkConnectionInterceptor(networkConnectivityManager: NetworkConnectivityManager): NetworkConnectionInterceptor = NetworkConnectionInterceptor(networkConnectivityManager)


  @Provides
  @Singleton
  fun provideOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor) = OkHttpClient.Builder()
      .addInterceptor(networkConnectionInterceptor)
      .addNetworkInterceptor(StethoInterceptor())
      .build()


  @Provides
  @Singleton
  fun provideRetrofitInstance(okHttpClient: OkHttpClient) = Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(UrlConstants.BASE_URL)
      .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
      .build()


  @Provides
  @Singleton
  fun providePinBookService(retrofit: Retrofit) = retrofit.create<PinBookService>()


}
