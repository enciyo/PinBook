package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.ViewState

data class DashboardViewState(
    var isLoadingPopularBooks: Boolean = false,
    var isLoadingShowcaseBooks: Boolean = false,
    var showCaseFailureMessage: String = "",
    var popularBooksFailureMessage: String = "",
    var isEmptyPopularBooks: Boolean = false,
    var isEmptyShowcaseBooks: Boolean = false,
    var showCaseBookTitleIndex: Int = 0
) : ViewState() {

    val isFailureShowCase: Boolean
        get() {
            return showCaseFailureMessage.isBlank().not()
        }

    val isFailurePopularBooks: Boolean
        get() {
            return popularBooksFailureMessage.isBlank().not()
        }






    fun getBookName(mutableList: MutableList<ShowcaseBooks>): String {
        return if (mutableList.isNullOrEmpty()) "" else mutableList[showCaseBookTitleIndex].name
            ?: ""
    }

    val isRefreshing
        get() = (isEmptyPopularBooks.not() && isEmptyShowcaseBooks.not()) && (isLoadingPopularBooks || isLoadingPopularBooks)

}


// DashboardViewState()
//StateFlow ->



