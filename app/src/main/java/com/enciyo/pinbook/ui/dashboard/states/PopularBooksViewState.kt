package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.SideEffect
import com.enciyo.pinbook.reducer.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed class PopularBooksViewState {
  data class PopularBooksSuccess(val popularBooks: MutableList<PopularBooks>) : PopularBooksViewState()
  object Loading : PopularBooksViewState()
  object Empty : PopularBooksViewState()
  object Failure : PopularBooksViewState()
}


data class DashboardViewState(
    var isLoadingPopularBooks: Boolean = false,
    var isLoadingShowcaseBooks: Boolean = false,
    var isEmptyPopularBooks: Boolean = false,
    var isEmptyShowcaseBooks: Boolean = false,
    var showCaseBookTitleIndex : Int = 0
) : ViewState() {

  fun getBookName(mutableList: MutableList<ShowcaseBooks>): String {
    return if (mutableList.isNullOrEmpty()) "" else mutableList[showCaseBookTitleIndex].name ?: ""
  }


  val isRefreshing
    get() = (isEmptyPopularBooks.not() && isEmptyShowcaseBooks.not()) && (isLoadingPopularBooks || isLoadingPopularBooks)

}


data class DashboardSideEffect(
    val popularBooks: StateFlow<MutableList<PopularBooks>> = MutableStateFlow(mutableListOf()),
    val showcaseBooks: StateFlow<MutableList<ShowcaseBooks>> = MutableStateFlow(mutableListOf())
) : SideEffect() {
  fun setPopularBooks(books: List<PopularBooks>) {
    (popularBooks as MutableStateFlow<MutableList<PopularBooks>>).value = books.toMutableList()
  }

  fun setShowCaseBooks(books: List<ShowcaseBooks>) {
    (showcaseBooks as MutableStateFlow<MutableList<ShowcaseBooks>>).value = books.toMutableList()
  }

}