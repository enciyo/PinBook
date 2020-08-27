package com.enciyo.pinbook.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.domain.usecases.FetchPopularBooksUseCase
import com.enciyo.pinbook.domain.usecases.FetchShowcaseBooksUseCase
import com.enciyo.pinbook.reducer.BaseViewModel
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.Redux
import com.enciyo.pinbook.ui.dashboard.states.DashboardActionState
import com.enciyo.pinbook.ui.dashboard.states.DashboardEvents
import com.enciyo.pinbook.ui.dashboard.states.DashboardRepoState
import com.enciyo.pinbook.ui.dashboard.states.DashboardViewState
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
class DashboardViewModel @ViewModelInject constructor(
    private val mFetchPopularBooksUseCase: FetchPopularBooksUseCase,
    private val mFetchShowcaseBooksUseCase: FetchShowcaseBooksUseCase,
    @Redux val mReducer: Reducer<DashboardViewState, DashboardActionState, DashboardRepoState>
) : BaseViewModel<DashboardViewState, DashboardActionState, DashboardRepoState, DashboardEvents>(mReducer) {



  override fun onEvent(event: DashboardEvents) {
    when (event) {
      is DashboardEvents.ScrolledShowCaseBooks -> viewTo(currentViewState().copy(showCaseBookTitleIndex = event.showCaseBook))
      is DashboardEvents.Init -> {
        fetchPopularBooks()
        fetchShowcaseBooks()
      }
      is DashboardEvents.SwipedToRefresh -> {
        fetchPopularBooks()
        fetchShowcaseBooks()
      }
      is DashboardEvents.NavigateTest -> {
        mReducer.actionTo(DashboardActionState.NavigateToTest)
      }
    }
  }


  private fun fetchShowcaseBooks() {
    mFetchShowcaseBooksUseCase.invoke()
        .onEach(::onShowcaseBooksResultHandle)
        .launchIn(viewModelScope)
  }

  private fun fetchPopularBooks() {
    mFetchPopularBooksUseCase.invoke()
        .onEach(::onPopularBooksResultHandle)
        .launchIn(viewModelScope)
  }


  private fun onShowcaseBooksResultHandle(result: AwesomeResult<List<ShowcaseBooks>>) {
    when (result) {
      is AwesomeResult.Success -> {
        val data = result.data.toMutableList()
        repoTo { it.showcaseBooks.value = data }
            .viewTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = data.isNullOrEmpty(), showCaseBookTitleIndex = 0))
      }
      is AwesomeResult.Loading -> viewTo(currentViewState().copy(isLoadingShowcaseBooks = true))
      is AwesomeResult.ServerError ->
        viewTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
            .actionTo(DashboardActionState.ShowErrorMessage(result.errorData?.message.toString()))
      is AwesomeResult.NoNetworkConnection ->
        viewTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
            .actionTo(DashboardActionState.ShowErrorMessageFromResource(R.string.NoConnection))
      is AwesomeResult.UnknownError ->
        viewTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
            .actionTo(DashboardActionState.ShowErrorMessageFromResource(R.string.UnknowError))
    }
  }

  private fun onPopularBooksResultHandle(result: AwesomeResult<List<PopularBooks>>) {
    when (result) {
      is AwesomeResult.Success -> {
        val showcaseBooksData = result.data.toMutableList()
        viewTo(currentViewState().copy(isLoadingPopularBooks = false, isEmptyPopularBooks = showcaseBooksData.isNullOrEmpty()))
            .repoTo { it.popularBooks.value = showcaseBooksData }
      }

      is AwesomeResult.Loading -> viewTo(currentViewState().copy(isLoadingPopularBooks = true))
      is AwesomeResult.ServerError -> viewTo(currentViewState().copy(isLoadingPopularBooks = false, isEmptyPopularBooks = false))
      is AwesomeResult.UnknownError -> viewTo(currentViewState().copy(isLoadingPopularBooks = false, isEmptyPopularBooks = false))
      is AwesomeResult.NoNetworkConnection -> viewTo(currentViewState().copy(isLoadingPopularBooks = false, isEmptyPopularBooks = false))
    }
  }

}


