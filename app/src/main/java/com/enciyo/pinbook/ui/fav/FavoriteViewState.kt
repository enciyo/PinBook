package com.enciyo.pinbook.ui.fav

import com.enciyo.pinbook.reducer.ViewState


data class FavoriteViewState(
    val isFavoriteBooksEmpty : Boolean = false,
    val currentUsername:String = "",
    val currentDisplayName:String = ""
):ViewState()