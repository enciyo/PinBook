package com.enciyo.pinbook.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUser(
    @SerialName("id")
    var id:Int? = null,
    @SerialName("displayName")
    val mDisplayName: String? = null, // Mustafa Kılıç
    @SerialName("email")
    val mEmail: String? = null, // m@hotmail.com
    @SerialName("password")
    val mPassword: String? = null, // 123456
    @SerialName("username")
    val mUsername: String? = null // enciyo
)


