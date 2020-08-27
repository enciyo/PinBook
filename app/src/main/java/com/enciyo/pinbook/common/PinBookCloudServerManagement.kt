package com.enciyo.pinbook.common

import com.enciyo.pinbook.domain.PinBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject


/**
 * If the Heroku.com server is off, it starts the server.
 * The server automatically shuts down when there is no request within 30 minutes.
 */
class PinBookCloudServerManagement @Inject constructor(private val mRepository: PinBookRepository) {

  private var isServerAvailable = false
  private var isErrorEmittedOneTime = false

  fun check() = flow {
    while (isServerAvailable.not()) {
      val response = mRepository.fetchCheckServerIsAvailable()
      handleServerIsAvailable(response)
    }
  }
      .catch {e->
          emit(AwesomeResult.UnknownError(e))
      }
      .flowOn(Dispatchers.IO)

  private suspend fun FlowCollector<AwesomeResult<ResponseBody>>.handleServerIsAvailable(response: AwesomeResult<ResponseBody>) {
    when (response) {
      is AwesomeResult.Success -> emit(response).also { isServerAvailable = true }
      else -> {
        delay(350)
        if (isErrorEmittedOneTime.not()) {
          emit(response)
          isErrorEmittedOneTime = true
        }
      }
    }
  }


}
