package com.enciyo.pinbook.ui.fav

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.User
import com.enciyo.pinbook.domain.usecases.FetchCurrentUserUseCase
import com.enciyo.pinbook.domain.usecases.FetchFavoriteBooksUseCase
import com.enciyo.pinbook.reducer.BaseViewModel
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.Redux
import com.enciyo.pinbook.utils.ioJob
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
class FavoriteViewModel @ViewModelInject constructor(
    private val fetchFavoriteBooksUseCase: FetchFavoriteBooksUseCase,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    @Redux val reducer: Reducer<FavoriteViewState, FavoriteActionState, FavoriteRepoState>
) : BaseViewModel<FavoriteViewState, FavoriteActionState, FavoriteRepoState, FavoriteUserInteractions>(reducer) {


  override fun onEvent(event: FavoriteUserInteractions) {
    when (event) {
      FavoriteUserInteractions.Init -> {
        fetchFavoriteBooks()
        fetchCurrentUserCase()
      }
    }
  }


  private fun fetchFavoriteBooks() {
    fetchFavoriteBooksUseCase.invoke()
        .onEach(::onHandleFavoriteBooksResult)
        .launchIn(viewModelScope)
  }

  private fun fetchCurrentUserCase() {
    ioJob {
      val result = fetchCurrentUserUseCase.invoke()
      handleCurrentUserResult(result)
    }
  }

  private fun handleCurrentUserResult(result: AwesomeResult<User>) {
    when (result) {
      is AwesomeResult.Success -> viewTo(currentViewState().copy(currentDisplayName = result.data.displayName
          ?: "", currentUsername = result.data.username ?: ""))
    }
  }


  private fun onHandleFavoriteBooksResult(result: AwesomeResult<List<PopularBooks>>) {
    when (result) {
      is AwesomeResult.Success -> {
        val resultData = result.data.toMutableList()
        repoTo { it.favoriteBooks.value = resultData }
            .viewTo(currentViewState().copy(
                isFavoriteBooksEmpty = resultData.isNullOrEmpty()
            ))
      }
    }
  }


}