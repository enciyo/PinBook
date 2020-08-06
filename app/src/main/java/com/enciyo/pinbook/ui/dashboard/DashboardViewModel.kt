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
import com.enciyo.pinbook.ui.dashboard.states.*
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
class DashboardViewModel @ViewModelInject constructor(
    private val mFetchPopularBooksUseCase: FetchPopularBooksUseCase,
    private val mFetchShowcaseBooksUseCase: FetchShowcaseBooksUseCase,
    @Redux private val mReducer: Reducer<DashboardViewState, DashboardActionState, DashboardSideEffect>
) : BaseViewModel<DashboardViewState, DashboardActionState, DashboardSideEffect, DashboardEvents>(mReducer) {

  private val _showCaseViewState: MutableStateFlow<Event<ShowCaseViewState>> = MutableStateFlow(Event(ShowCaseViewState.Empty))
  val showCaseViewState: StateFlow<Event<ShowCaseViewState>>
    get() = _showCaseViewState

  private val _popularBooksViewState: MutableStateFlow<Event<PopularBooksViewState>> = MutableStateFlow(Event(PopularBooksViewState.Empty))
  val popularBooksViewState: StateFlow<Event<PopularBooksViewState>>
    get() = _popularBooksViewState

  private val _actionState: MutableStateFlow<Event<DashboardActionState>> = MutableStateFlow(Event(DashboardActionState.ChangeShowCaseTitle("Yükleniyor")))
  val actionState: StateFlow<Event<DashboardActionState>>
    get() = _actionState


  private fun dispatch(action: DashboardActionState?) {
    action?.let { _actionState.value = Event(it) }
  }

  private fun postState(viewState: PopularBooksViewState, action: DashboardActionState? = null) {
    _popularBooksViewState.value = Event(viewState)
    dispatch(action)
  }

  private fun postState(viewState: ShowCaseViewState, action: DashboardActionState? = null) {
    _showCaseViewState.value = Event(viewState)
    dispatch(action)
  }

  val popularBooksCurrentState
    get() = _popularBooksViewState.value.peekContent()

  val showCaseCurrentState
    get() = _showCaseViewState.value.peekContent()


  override fun onEvent(event: DashboardEvents) {
    when (event) {
      is DashboardEvents.ScrolledShowCaseBooks -> moveTo(currentViewState().copy(showCaseBookTitleIndex = event.showCaseBook))
      is DashboardEvents.Init -> {
        fetchPopularBooks()
        fetchShowcaseBooks()
      }
      is DashboardEvents.SwipedToRefresh -> {
        fetchPopularBooks()
        fetchShowcaseBooks()
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
        val data = result.data
        currentSideEffect().setShowCaseBooks(data)
        moveTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = data.isNullOrEmpty()))
      }
      is AwesomeResult.Loading -> moveTo(currentViewState().copy(isLoadingShowcaseBooks = true))
      is AwesomeResult.ServerError -> moveTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
          .actionTo(DashboardActionState.ShowErrorMessage(result.errorData?.message.toString()))
      is AwesomeResult.NoNetworkConnection -> moveTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
          .actionTo(DashboardActionState.ShowErrorMessageFromResource(R.string.NoConnection))
      is AwesomeResult.UnknownError -> moveTo(currentViewState().copy(isLoadingShowcaseBooks = false, isEmptyShowcaseBooks = true))
          .actionTo(DashboardActionState.ShowErrorMessageFromResource(R.string.UnknowError))
    }

  }


  private fun onPopularBooksResultHandle(result: AwesomeResult<List<PopularBooks>>) {
    when (result) {
      is AwesomeResult.Success -> {
        moveTo(currentViewState().copy(isLoadingPopularBooks = false,isEmptyPopularBooks = result.data.isNullOrEmpty()))
            .currentSideEffect().setPopularBooks(result.data)
      }
      is AwesomeResult.Loading -> moveTo(currentViewState().copy(isLoadingPopularBooks = true))
      is AwesomeResult.ServerError -> moveTo(currentViewState().copy(isLoadingPopularBooks = false,isEmptyPopularBooks = false))
      is AwesomeResult.UnknownError -> moveTo(currentViewState().copy(isLoadingPopularBooks = false,isEmptyPopularBooks = false))
      is AwesomeResult.NoNetworkConnection -> moveTo(currentViewState().copy(isLoadingPopularBooks = false,isEmptyPopularBooks = false))
    }
  }


}

