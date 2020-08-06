package com.enciyo.pinbook.data.remote.interceptors

import com.enciyo.pinbook.common.network.NetworkConnectivityManager
import com.enciyo.pinbook.data.remote.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response


class NetworkConnectionInterceptor (private val mNetworkConnectivityManager: NetworkConnectivityManager) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    if (mNetworkConnectivityManager.isNetworkAvailable.not()){
      throw NoConnectivityException()
    }
    chain.request().newBuilder().also {
      return chain.proceed(it.build())
    }
  }

}
