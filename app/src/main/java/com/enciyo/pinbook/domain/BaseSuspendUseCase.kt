package com.enciyo.pinbook.domain

import com.enciyo.pinbook.common.AwesomeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseSuspendUseCase<in P, S : Any> {

    suspend operator fun invoke(parameters: P? = null): AwesomeResult<S> {
       return withContext(Dispatchers.IO){
            return@withContext try {
                execute(parameters)
            } catch (e: Exception) {
                AwesomeResult.UnknownError(e)
            }
        }

    }
    abstract suspend fun execute(parameters: P? = null): AwesomeResult<S>

}

