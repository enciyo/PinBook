package com.enciyo.pinbook.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentDashboardBinding
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.ui.dashboard.adapters.DashboardShowCaseAdapter
import com.enciyo.pinbook.ui.dashboard.states.DashboardActionState
import com.enciyo.pinbook.ui.dashboard.states.DashboardEvents
import com.enciyo.pinbook.ui.dashboard.states.DashboardRepoState
import com.enciyo.pinbook.ui.dashboard.states.DashboardViewState
import com.enciyo.pinbook.utils.addOnPageChangeListener
import com.enciyo.pinbook.utils.onEach
import com.enciyo.pinbook.utils.textWithFadeAnimation
import com.enciyo.pinbook.utils.viewbinding.viewBinding
import com.enciyo.pinbook.utils.viewpager.ZoomFadeTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    @Inject
    lateinit var mPinToast: PinToast


    private val mBinding: FragmentDashboardBinding by viewBinding()
    private val mViewModel: DashboardViewModel by viewModels()
    private val mDashboardShowCaseAdapter: DashboardShowCaseAdapter = DashboardShowCaseAdapter()
    private val mBooksAdapter: BooksAdapter = BooksAdapter().also {
      it.mListener = createAdapterListener()
    }

    private val savedStateHandle
        get() = findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("isRated")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViewPagerPageTransformer()


        savedStateHandle?.observe(viewLifecycleOwner,::onCurrentBackStackEntryObserver)

        with(mBinding) {
            viewPagerShowCase.addOnPageChangeListener(::onShowCasePageChanged)
            root.setOnRefreshListener(::onSwipedToRefresh)
            recyclerViewPopularBooks.adapter = mBooksAdapter
        }

        with(mViewModel) {
            render(
              lifecycle = viewLifecycleOwner,
              onState = ::renderViewState,
              onActionIfNotHandled = ::renderIfNotHandledAction,
              onRepo = ::renderRepoState
            )
            onEvent(DashboardEvents.Init)
        }

    }


    private fun onCurrentBackStackEntryObserver(isRated: Boolean){
        if (isRated)
            mViewModel.onEvent(DashboardEvents.SwipedToRefresh)
    }


    private fun createAdapterListener(): (BooksAdapter.Companion.BooksAdapterEvents) -> Unit = {
      when(it){
        is BooksAdapter.Companion.BooksAdapterEvents.Clicked -> {
            findNavController().navigate(R.id.actionToDetail, bundleOf(
                "Books" to it.books
            ))
        }
      }
    }

    private fun renderViewState(viewState: DashboardViewState) {
        with(mBinding) {
            progressBarPopularBooks.isVisible = viewState.isLoadingPopularBooks
            progressBarShowCase.isVisible = viewState.isLoadingPopularBooks

            swipeRefreshLayout.isRefreshing = viewState.isRefreshing
            textViewShowCaseBookName.textWithFadeAnimation = viewState.getBookName(mDashboardShowCaseAdapter.currentItems)

            viewPagerShowCase.currentItem = viewState.showCaseBookTitleIndex
            viewPagerShowCase.isVisible = viewState.isFailureShowCase.not()

            recyclerViewPopularBooks.isVisible = viewState.isFailurePopularBooks.not()

            textViewShowCaseError.isVisible = viewState.isFailurePopularBooks
            textViewShowCaseError.textWithFadeAnimation = viewState.showCaseFailureMessage

            textViewPopularBooksError.isVisible = viewState.isFailurePopularBooks
            textViewPopularBooksError.textWithFadeAnimation = viewState.popularBooksFailureMessage

        }
    }

    private fun renderIfNotHandledAction(actionState: DashboardActionState) {
        when (actionState) {
          is DashboardActionState.ShowErrorMessageFromResource -> mPinToast.showErrorMessage(
            actionState.message
          )
          is DashboardActionState.NavigateToTest -> findNavController().navigate(R.id.actionDashboardToFav)
        }
    }

    private fun renderRepoState(repoState: DashboardRepoState) {

        repoState
            .popularBooks
            .asFlow()
            .onEach(::renderPopularBooks)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        repoState
            .showcaseBooks
            .asFlow()
            .onEach(::renderShowcaseBooks)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun renderPopularBooks(popularBooks: MutableList<PopularBooks>) {
        mBooksAdapter.submitList(popularBooks)
    }

    private fun renderShowcaseBooks(showcaseBooks: MutableList<ShowcaseBooks>) {
        initViewPagerAdapter(showcaseBooks)
        initFirstShowCaseUI(showcaseBooks.firstOrNull() ?: return)
    }


    private fun onSwipedToRefresh() {
        mViewModel.onEvent(DashboardEvents.SwipedToRefresh)
    }

    private fun onShowCasePageChanged(position: Int) {
        mViewModel.onEvent(DashboardEvents.ScrolledShowCaseBooks(position))
    }


    private fun initViewPagerAdapter(showCaseList: MutableList<ShowcaseBooks>) {
        mBinding.viewPagerShowCase.adapter = null
        mDashboardShowCaseAdapter.currentItems = showCaseList
        mBinding.viewPagerShowCase.adapter = mDashboardShowCaseAdapter
    }

    private fun initFirstShowCaseUI(showCase: ShowcaseBooks) {
        mBinding.textViewShowCaseBookName.textWithFadeAnimation = showCase.name
    }

    private fun initViewPagerPageTransformer() {
        val paddingPx = 300
        mBinding.viewPagerShowCase.setPadding(paddingPx, 0, paddingPx, 0)
        mBinding.viewPagerShowCase.setPageTransformer(
          false,
          ZoomFadeTransformer(paddingPx, 0.8f, 1f)
        )
    }


}