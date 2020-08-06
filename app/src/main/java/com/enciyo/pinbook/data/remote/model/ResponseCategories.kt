package com.enciyo.pinbook.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCategories(
    @SerialName("categoryName")
    val mCategoryName: String? = null, // Felsefe
    @SerialName("id")
    val mId: Int? = null // 1
)