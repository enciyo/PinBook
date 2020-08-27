package com.enciyo.pinbook.ui.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.Category
import com.enciyo.pinbook.domain.usecases.FetchAllBookUseCase
import com.enciyo.pinbook.domain.usecases.FetchBookCategories
import com.enciyo.pinbook.domain.usecases.SearchBooksUseCase
import com.enciyo.pinbook.reducer.BaseViewModel
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.Redux
import com.enciyo.pinbook.ui.books.state.BookActionState
import com.enciyo.pinbook.ui.books.state.BookInteractions
import com.enciyo.pinbook.ui.books.state.BookRepoState
import com.enciyo.pinbook.ui.books.state.BookViewState
import com.enciyo.pinbook.utils.ioJob
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
class BooksViewModel @ViewModelInject constructor(
  private val mFetchAllBookUseCase: FetchAllBookUseCase,
  private val mFetchBookCategories: FetchBookCategories,
  private val mSearchBooksUseCase: SearchBooksUseCase,
  @Redux val mReducer: Reducer<BookViewState, BookActionState, BookRepoState>
) : BaseViewModel<BookViewState, BookActionState, BookRepoState, BookInteractions>(mReducer) {


    override fun onEvent(event: BookInteractions) {
        when (event) {
          is BookInteractions.Init -> {
            fetchBooks()
            fetchCategories()
          }
          is BookInteractions.OnCategoriseClicked -> fetchFilteredBooks(event.categoryId)
         
        }
    }

  
    private fun fetchBooks() {
        mFetchAllBookUseCase.invoke()
            .onEach(::onHandleResultBooks)
            .launchIn(viewModelScope)
    }

    private fun fetchCategories() {
        ioJob {
            mFetchBookCategories.invoke().let(::onHandleBookCategories)
        }
    }

    private fun fetchFilteredBooks(categoryId: String) {
        mSearchBooksUseCase.invoke(categoryId)
            .onEach(::onHandleSearchResult)
            .launchIn(viewModelScope)
    }


    private fun onHandleSearchResult(result: AwesomeResult<List<Book>>) {
        when (result) {
          is AwesomeResult.Success -> {
            viewTo(
              currentViewState().copy(
                isLoading = false,
              )
            )
            repoTo { it.books.value = result.data }
          }
          is AwesomeResult.Loading -> viewTo(currentViewState().copy(isLoading = true))
          is AwesomeResult.UnknownError -> viewTo(
            currentViewState().copy(
              isLoading = false,
              errorMessage = result.message
            )
          )
          is AwesomeResult.ServerError -> viewTo(
            currentViewState().copy(
              isLoading = false,
              errorMessage = result.errorData?.message
            )
          )
          is AwesomeResult.NoNetworkConnection -> viewTo(currentViewState().copy(isLoading = false))
        }
    }


    private fun onHandleBookCategories(result: AwesomeResult<List<Category>>) {
        when (result) {
          is AwesomeResult.Success -> {
            viewTo(currentViewState().copy(isSearchFocus = false))
              .repoTo { it.categories.value = result.data }
          }
        }
    }


    private fun onHandleResultBooks(result: AwesomeResult<List<Book>>) {
        when (result) {
          is AwesomeResult.Success -> {
            val data = result.data
            viewTo(currentViewState().copy(isLoading = false, isSearchFocus = false))
              .repoTo { it.books.value = data }
          }
          is AwesomeResult.Loading -> viewTo(currentViewState().copy(isLoading = true))
          is AwesomeResult.UnknownError -> viewTo(
            currentViewState().copy(
              isLoading = false,
              errorMessage = result.message
            )
          )
          is AwesomeResult.ServerError -> viewTo(
            currentViewState().copy(
              isLoading = false,
              errorMessage = result.errorData?.message
            )
          )
          is AwesomeResult.NoNetworkConnection -> viewTo(currentViewState().copy(isLoading = false))
        }
    }


}