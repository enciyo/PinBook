package com.enciyo.pinbook.ui.books

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentBooksBinding
import com.enciyo.pinbook.domain.mapper.toPopularBooks
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.Category
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.ui.books.state.BookActionState
import com.enciyo.pinbook.ui.books.state.BookInteractions
import com.enciyo.pinbook.ui.books.state.BookRepoState
import com.enciyo.pinbook.ui.books.state.BookViewState
import com.enciyo.pinbook.utils.hideKeyboard
import com.enciyo.pinbook.utils.onEach
import com.enciyo.pinbook.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BooksFragment : Fragment(R.layout.fragment_books), SearchView.OnQueryTextListener {

    private val mViewModel: BooksViewModel by viewModels()
    private val mBinding: FragmentBooksBinding by viewBinding()
    private val mBooksAdapter = BooksAdapter().also { it.mListener = mBooksAdapterListener() }

    private fun mBooksAdapterListener(): (BooksAdapter.Companion.BooksAdapterEvents) -> Unit = {
        when(it){
            is BooksAdapter.Companion.BooksAdapterEvents.Clicked -> {
                findNavController().navigate(R.id.actionToDetail, bundleOf("Books" to it.books))
            }
        }
    }

    private val mCategoriesAdapter = CategoryAdapter()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mBinding) {
            recyclerViewBooks.adapter = mBooksAdapter
            searchViewBookQuery.setOnQueryTextListener(this@BooksFragment)
        }

        with(mViewModel) {
            render(
                lifecycle = viewLifecycleOwner,
                onState = ::renderViewState,
                onRepo = ::renderOnRepoState,
                onActionIfNotHandled = ::renderIfNotHandledActionState
            )
        }

        mViewModel.onEvent(BookInteractions.Init)

    }


    private fun renderOnRepoState(bookRepoState: BookRepoState) {
        with(bookRepoState) {

            books.asFlow()
                .onEach(::renderBooks)
                .launchIn(viewLifecycleOwner.lifecycleScope)

            categories.asFlow()
                .onEach(::renderCategories)
                .launchIn(viewLifecycleOwner.lifecycleScope)

            filteredBooks.asFlow()
                .onEach(::renderFilteredBooks)
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }


    private fun renderFilteredBooks(list: List<Book>) {
        mBooksAdapter.setList(list.toPopularBooks().toMutableList())
    }

    private fun renderBooks(list: List<Book>) {
        mBooksAdapter.setList(list.toPopularBooks().toMutableList())
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(350)
            mBinding.recyclerViewBooks.smoothScrollToPosition(0)
        }

    }

    private fun renderCategories(list: List<Category>) {
        val spanCount = calculateCategorySpanCount(list.size)
        initUICategory(spanCount)
        initCategoryAdapter(list)
    }


    private fun initUICategory(spanCount: Int) {
        with(mBinding.recyclerViewCategories) {
            adapter = mCategoriesAdapter
            layoutManager = StaggeredGridLayoutManager(spanCount, RecyclerView.HORIZONTAL)
        }
    }

    private fun initCategoryAdapter(list: List<Category>) {
        with(mCategoriesAdapter) {
            submitList(list)
            listener = adapterClickListener()
        }
    }

    private fun adapterClickListener(): ((CategoryAdapter.Companion.CategoryAdapterEvent) -> Unit)? = {
        hideKeyboard()
        mBinding.searchViewBookQuery.setQuery(null,false)
        when(it){
            is CategoryAdapter.Companion.CategoryAdapterEvent.OnSelected -> {
                mViewModel.onEvent(BookInteractions.OnCategoriseClicked(it.category.id))
            }
            is CategoryAdapter.Companion.CategoryAdapterEvent.NonSelected -> {
                mViewModel.onEvent(BookInteractions.Init)
            }
        }
    }

    private fun calculateCategorySpanCount(size: Int): Int {
        var spanCountDivide = size / 4
        if (spanCountDivide <= 0) spanCountDivide = 1
        return spanCountDivide
    }


    private fun renderIfNotHandledActionState(bookActionState: BookActionState) {
        when (bookActionState) {
            else -> Unit
        }
    }

    private fun renderViewState(bookViewState: BookViewState) {


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mBooksAdapter.filter.filter(newText)
        return true
    }

}