package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.ViewState

data class DashboardViewState(
    var isLoadingPopularBooks: Boolean = false,
    var isLoadingShowcaseBooks: Boolean = false,
    var isEmptyPopularBooks: Boolean = false,
    var isEmptyShowcaseBooks: Boolean = false,
    var showCaseBookTitleIndex: Int = 0
) : ViewState() {

  fun getBookName(mutableList: MutableList<ShowcaseBooks>): String {
    return if (mutableList.isNullOrEmpty()) "" else mutableList[showCaseBookTitleIndex].name ?: ""
  }

  val isRefreshing
    get() = (isEmptyPopularBooks.not() && isEmptyShowcaseBooks.not()) && (isLoadingPopularBooks || isLoadingPopularBooks)

}


// DashboardViewState()
//StateFlow ->



