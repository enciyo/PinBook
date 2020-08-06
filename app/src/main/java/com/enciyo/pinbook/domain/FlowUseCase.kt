package com.enciyo.pinbook.domain

import com.enciyo.pinbook.common.AwesomeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart


abstract class FlowUseCase<in P, S:Any> {


  operator fun invoke(parameters: P? = null): Flow<AwesomeResult<S>> {

    return execute(parameters)
        .catch { e -> emit(AwesomeResult.UnknownError(e)) }
        .flowOn(Dispatchers.IO)
        .onStart {emit(AwesomeResult.Loading(true))}
  }

  abstract fun execute(parameters: P? = null): Flow<AwesomeResult<S>>
}


