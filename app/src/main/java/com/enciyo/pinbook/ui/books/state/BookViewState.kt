package com.enciyo.pinbook.ui.books.state

import com.enciyo.pinbook.reducer.ViewState


data class BookViewState(
    var isLoading: Boolean = false,
    var isSearchFocus: Boolean = false,
    var errorMessage:String? = null,
) : ViewState()