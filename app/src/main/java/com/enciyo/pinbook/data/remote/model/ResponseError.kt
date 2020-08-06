package com.enciyo.pinbook.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseError(
    @SerialName("error")
    val mError: String? = null, // Internal Server GeneralError
    @SerialName("message")
    val mMessage: String? = null, // Kullanıcı adı veya şifre yanlış
    @SerialName("path")
    val mPath: String? = null, // /api/login
    @SerialName("status")
    val mStatus: Int? = null, // 500
    @SerialName("timestamp")
    val mTimestamp: String? = null // 2020-07-30T15:32:26.912+0000
)