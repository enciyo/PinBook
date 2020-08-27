package com.enciyo.pinbook.data

import android.util.Log
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.data.remote.NoConnectivityException
import com.enciyo.pinbook.data.remote.model.ResponseError
import com.enciyo.pinbook.domain.mapper.toServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import kotlin.reflect.KFunction0
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1


@ExperimentalCoroutinesApi
object DataManager {


    fun <S> syncData(
        fromDB: KFunction0<Flow<S>>,
        fromApi: KSuspendFunction0<AwesomeResult<S>>,
        insert: KSuspendFunction1<S, Unit>,
        delete: KSuspendFunction1<S, Unit>
    ) = channelFlow<AwesomeResult<S>> {
        fromDB()
            .map { AwesomeResult.Success(it, false) }
            .onEach { offer(it) }
            .launchIn(this)
        when (val response = fromApi()) {
            is AwesomeResult.Success -> {
                delete(response.data)
                insert(response.data)
            }
            else -> send(response)
        }
        awaitClose { this.cancel() }
    }.flowOn(Dispatchers.IO)


    fun <S> fetchDataFromDbIfNoNetworkConnection(
        fromDB: KFunction0<Flow<S>>,
        fromApi: KSuspendFunction0<AwesomeResult<S>>,
        insert: KSuspendFunction1<S, Unit>,
        delete: KSuspendFunction1<S, Unit>
    ) = flow<AwesomeResult<S>> {
        when (val response = fromApi()) {
            is AwesomeResult.Success -> {
                delete(response.data)
                insert(response.data)
                emitAll(fromDB().map { AwesomeResult.Success(it, true) })
            }
            else -> emitAll(fromDB().map { AwesomeResult.Success(it, false) })
        }
    }


    suspend fun <T> fetch(request: suspend () -> Response<T>): AwesomeResult<T> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = request.invoke()
                val errorResponse = response?.errorBody()?.charStream()?.readText()
                return@withContext when {
                    response.isSuccessful && response.body() != null -> AwesomeResult.Success(
                        response.body()!!,
                        true
                    )
                    errorResponse != null -> errorResponse.toServerError()
                    else -> AwesomeResult.UnknownError(java.lang.Exception(response.message()))
                }
            } catch (e: Exception) {
                Log.i("ServerError", e.message.toString())
                when (e) {
                    is NoConnectivityException -> AwesomeResult.NoNetworkConnection
                    is HttpException -> e.response()?.errorBody()?.string().toServerError()
                    else -> AwesomeResult.UnknownError(e)
                }
            }
        }

    private fun String?.toServerError(): AwesomeResult.ServerError {
        val error = try {
            Json.decodeFromString(ResponseError.serializer(),this.toString())
        } catch (e: java.lang.Exception) {
            null
        }
        return AwesomeResult.ServerError(error?.toServerError())
    }


}
