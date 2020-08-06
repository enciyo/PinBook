package com.enciyo.pinbook.common

import com.enciyo.pinbook.domain.model.GeneralError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class AwesomeResult<out S> {
  data class Success<out S>(val data: S, val isApi: Boolean) : AwesomeResult<S>()
  data class Loading(val isLoading: Boolean? = true) : AwesomeResult<Nothing>()
  data class UnknownError(val exception: Throwable, val message: String = exception.localizedMessage) : AwesomeResult<Nothing>()
  data class ServerError(val errorData: GeneralError?) : AwesomeResult<Nothing>()
  object NoNetworkConnection : AwesomeResult<Nothing>()
}


inline fun <S, T> AwesomeResult<S>.map(transform: (S) -> T): AwesomeResult<T> = when (this) {
  is AwesomeResult.Success -> AwesomeResult.Success(transform.invoke(this.data), this.isApi)
  is AwesomeResult.Loading -> AwesomeResult.Loading(this.isLoading)
  is AwesomeResult.UnknownError -> AwesomeResult.UnknownError(this.exception)
  is AwesomeResult.ServerError -> AwesomeResult.ServerError(this.errorData)
  is AwesomeResult.NoNetworkConnection -> AwesomeResult.NoNetworkConnection
}

inline fun <S, T> AwesomeResult<List<S>>.mapAllList(transform: (S) -> T): AwesomeResult<List<T>> = when (this) {
  is AwesomeResult.Success -> AwesomeResult.Success(this.data.map { transform(it) }, this.isApi)
  is AwesomeResult.Loading -> AwesomeResult.Loading(this.isLoading)
  is AwesomeResult.UnknownError -> AwesomeResult.UnknownError(this.exception)
  is AwesomeResult.ServerError -> AwesomeResult.ServerError(this.errorData)
  is AwesomeResult.NoNetworkConnection -> AwesomeResult.NoNetworkConnection
}

inline fun <S, T> Flow<AwesomeResult<List<S>>>.mapFlowList(crossinline transform: (S) -> T) = this.map {
  it.map {
    it.map(transform)
  }
}