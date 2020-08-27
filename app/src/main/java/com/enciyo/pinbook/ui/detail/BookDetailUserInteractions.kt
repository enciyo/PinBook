package com.enciyo.pinbook.ui.detail

import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.reducer.UserInteractions

sealed class BookDetailUserInteractions : UserInteractions(){
    data class Init(val bookId:Int): BookDetailUserInteractions()
    data class RateBook(val bookId: Int,val rating:Double,val comment:String) :BookDetailUserInteractions()
    data class Refresh(val bookId: Int) : BookDetailUserInteractions()
    data class OnClickedFav(val book: PopularBooks): BookDetailUserInteractions()
}