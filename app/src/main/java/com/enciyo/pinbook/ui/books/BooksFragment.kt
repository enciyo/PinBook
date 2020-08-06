package com.enciyo.pinbook.ui.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentBooksBinding
import com.enciyo.pinbook.utils.dummyCategories
import com.enciyo.pinbook.utils.dummyPopularBooks

class BooksFragment : Fragment(R.layout.fragment_books) {

  private val mBinding: FragmentBooksBinding by viewBinding()
  private val mViewModel: BooksViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    BooksAdapter().apply {
      submitList(dummyPopularBooks)
    }.also {
      mBinding.recyclerViewBooks.adapter = it
    }

    CategoryAdapter().apply {
      submitList(dummyCategories)
    }.also {
      mBinding.recyclerViewCategories.adapter = it
    }

  }

}