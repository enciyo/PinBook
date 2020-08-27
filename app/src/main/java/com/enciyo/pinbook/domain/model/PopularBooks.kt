package com.enciyo.pinbook.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PopularBooks(
    val id:Int,
    val imageUrl:String,
    val reviewCount:String,
    val rating:String,
    val name:String,
    val author:String
):Parcelable