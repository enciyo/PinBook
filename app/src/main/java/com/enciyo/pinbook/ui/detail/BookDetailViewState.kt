package com.enciyo.pinbook.ui.detail

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.enciyo.pinbook.R
import com.enciyo.pinbook.reducer.ViewState


data class BookDetailViewState(
    val isLoading: Boolean = false,
    val isCommentEnable : Boolean = true,
    val isFavorite: Boolean = false,
    val ratingBar: Float = 0.0f
) : ViewState(){

    fun getFavoriteBackgroundColor(context: Context) = if (isFavorite) ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_600))
    else ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))

}