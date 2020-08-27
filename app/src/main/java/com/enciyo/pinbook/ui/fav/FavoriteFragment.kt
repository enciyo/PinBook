package com.enciyo.pinbook.ui.fav

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentFavoriteBinding
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.utils.onEach
import com.enciyo.pinbook.utils.textWithFadeAnimation
import com.enciyo.pinbook.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

  private val mBinding: FragmentFavoriteBinding by viewBinding()
  private val mViewModel: FavoriteViewModel by viewModels()
  private val mBookAdapter: BooksAdapter = BooksAdapter()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(mBinding) {
      recyclerViewFavoriteBooks.adapter = mBookAdapter
    }

    with(mViewModel) {
      render(
          viewLifecycleOwner,
          onState = ::renderViewState,
          onActionIfNotHandled = ::renderActionState,
          onRepo = ::renderRepoState
      )
    }

    mViewModel.onEvent(FavoriteUserInteractions.Init)
  }

  private fun renderViewState(favoriteViewState: FavoriteViewState) = with(mBinding) {
    textViewDisplayName.textWithFadeAnimation = favoriteViewState.currentDisplayName
    textViewUsername.textWithFadeAnimation = favoriteViewState.currentUsername
  }

  private fun renderActionState(favoriteActionState: FavoriteActionState) {
    when (favoriteActionState) {
      FavoriteActionState.Init -> Unit
    }
  }

  private fun renderRepoState(favoriteRepoState: FavoriteRepoState) {
    favoriteRepoState.favoriteBooks.onEach(::renderFavoriteBooks).launchIn(viewLifecycleOwner.lifecycleScope)
  }

  private fun renderFavoriteBooks(data: MutableList<PopularBooks>) {
    mBookAdapter.submitList(data.map { it }.toMutableList())
  }



}