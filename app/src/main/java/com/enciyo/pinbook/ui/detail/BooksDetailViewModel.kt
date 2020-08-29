package com.enciyo.pinbook.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.BookDetail
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.usecases.*
import com.enciyo.pinbook.reducer.BaseViewModel
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.Redux
import com.enciyo.pinbook.utils.ioJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.*

@ExperimentalCoroutinesApi
class BooksDetailViewModel @ViewModelInject constructor(
    private val mFetchBookDetailUseCase: FetchBookDetailUseCase,
    private val mRateBookUseCase: RateBookUseCase,
    private val mInsertFavoriteBookUseCase: InsertFavoriteBookUseCase,
    private val mDeleteFavoriteBookUseCase: DeleteFavoriteBookUseCase,
    private val mFetchFavoriteBookUseCase: FetchFavoriteBookUseCase,
    @Redux val reducer: Reducer<BookDetailViewState, BookDetailActionState, BookDetailRepoState>
) : BaseViewModel<BookDetailViewState, BookDetailActionState, BookDetailRepoState, BookDetailUserInteractions>(
    reducer
) {


    override fun onEvent(event: BookDetailUserInteractions) {
        when (event) {
            is BookDetailUserInteractions.Init -> {
                fetchBookDetail(event.bookId)
                fetchPopularBookAndCheckIsFav(event.bookId)
            }
            is BookDetailUserInteractions.RateBook -> rateBook(event)
            is BookDetailUserInteractions.Refresh -> fetchBookDetail(event.bookId)
            is BookDetailUserInteractions.OnClickedFav -> clickedFavoriteBook(event.book)
        }
    }



    private fun fetchPopularBookAndCheckIsFav(bookId: Int){
        viewModelScope.launch {
            when(mFetchFavoriteBookUseCase.invoke(bookId.toString())){
                is AwesomeResult.Success -> viewTo(currentViewState().copy(isFavorite = true))
                else -> currentViewState().copy(isFavorite = false)
            }
        }
    }


    private fun clickedFavoriteBook(popularBooks: PopularBooks) {
        viewModelScope.launch {
            if (currentViewState().isFavorite.not())
                insertFavoriteBook(mInsertFavoriteBookUseCase.invoke(popularBooks))
            else
                deleteFavoriteBook(mDeleteFavoriteBookUseCase.invoke(popularBooks))
        }
    }

    private fun insertFavoriteBook(result: AwesomeResult<PopularBooks>) {
        when (result) {
            is AwesomeResult.Success -> viewTo(currentViewState().copy(isFavorite = true))
                .actionTo(BookDetailActionState.ShowSuccessAddedFavoriteBook)
            is AwesomeResult.Loading -> Unit
            else -> viewTo(currentViewState().copy(isFavorite = false))
        }
    }

    private fun deleteFavoriteBook(result: AwesomeResult<Boolean>) {
        when (result) {
            is AwesomeResult.Success -> viewTo(currentViewState().copy(isFavorite = false))
                .actionTo(BookDetailActionState.ShowSuccessRemovedFavoriteBook)
            else -> viewTo(currentViewState().copy(isFavorite = true))
        }
    }

    private fun fetchBookDetail(bookId: Int) {
        ioJob {
            mFetchBookDetailUseCase.invoke(bookId)
                .let(::handleBookDetailResult)
        }
    }

    private fun rateBook(event: BookDetailUserInteractions.RateBook) {
        viewTo(currentViewState().copy(isCommentEnable = false, ratingBar = event.rating.toFloat()))
        ioJob {
            mRateBookUseCase
                .invoke(
                    RateBookUseCase.RateBookUseCaseParams(
                        comment = event.comment,
                        bookId = event.bookId.toString(),
                        rating = event.rating
                    )
                ).let {
                    handleRateBookResult(it, event.bookId)
                }
        }
    }

    private fun handleRateBookResult(rateBook: AwesomeResult<ResponseBody>, bookId: Int) {
        when (rateBook) {
            is AwesomeResult.Success -> {
                repoTo { it.commentResult.value = UUID.randomUUID().toString() }
            }
        }
    }


    private fun handleBookDetailResult(bookDetail: AwesomeResult<BookDetail>) {
        when (bookDetail) {
            is AwesomeResult.Success -> {
                viewTo(
                    currentViewState().copy(
                        isLoading = false,
                        ratingBar = bookDetail.data.rating.toDouble().toFloat()
                    )
                )
                    .repoTo {
                        it.bookDetail.value = bookDetail.data
                    }
            }
        }
    }


}