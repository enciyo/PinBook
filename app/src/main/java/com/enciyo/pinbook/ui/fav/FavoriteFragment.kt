package com.enciyo.pinbook.ui.fav

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentFavoriteBinding
import com.enciyo.pinbook.domain.model.PopularBooks

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

  private val mBinding: FragmentFavoriteBinding by viewBinding()
  private val mViewModel:FavoriteViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    mBinding.recyclerViewFavoriteBooks.adapter = BooksAdapter().apply {
      submitList(dummyPopularBooks)
    }
  }


  private val dummyPopularBooks = mutableListOf<PopularBooks>(
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      ),
      PopularBooks(
          "https://m.media-amazon.com/images/I/512qnExA+NL.jpg",
          "123",
          "2.4",
          "Metafizi",
          "21321"
      )
  )


}