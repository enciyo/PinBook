package com.enciyo.pinbook.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.common.adapter.BooksAdapter
import com.enciyo.pinbook.databinding.FragmentDashboardBinding
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.ui.dashboard.adapters.DashboardShowCaseAdapter
import com.enciyo.pinbook.ui.dashboard.states.*
import com.enciyo.pinbook.utils.addOnPageChangeListener
import com.enciyo.pinbook.utils.textWithFadeAnimation
import com.enciyo.pinbook.utils.viewpager.ZoomFadeTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

  @Inject
  lateinit var mPinToast: PinToast


  private val mBinding: FragmentDashboardBinding by viewBinding()
  private val mViewModel: DashboardViewModel by viewModels()
  private val mDashboardShowCaseAdapter: DashboardShowCaseAdapter = DashboardShowCaseAdapter()
  private val mBooksAdapter: BooksAdapter = BooksAdapter()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViewPagerPageTransformer()

    with(mViewModel) {
      state
          .render(
              onState = ::renderViewState,
              onAction =::actionStateObserver,
              onSideEffect = ::renderSideEffectState
          )
          .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    with(mBinding) {
      viewPagerShowCase.addOnPageChangeListener(::onShowCasePageChanged)
      root.setOnRefreshListener(::onSwipedToRefresh)
      recyclerViewPopularBooks.adapter = mBooksAdapter

    }

    mViewModel.onEvent(DashboardEvents.Init)
  }

  private fun renderViewState(viewState: DashboardViewState){
    with(mBinding){
      mBinding.progressBarPopularBooks.isVisible = viewState.isLoadingPopularBooks
      mBinding.progressBarShowCase.isVisible = viewState.isLoadingPopularBooks
      mBinding.swipeRefreshLayout.isRefreshing = viewState.isRefreshing
      mBinding.textViewShowCaseBookName.textWithFadeAnimation = viewState.getBookName(mDashboardShowCaseAdapter.currentItems)
    }

  }

  private fun renderSideEffectState(sideEffect: DashboardSideEffect){
    sideEffect.showcaseBooks.onEach {
      initViewPagerAdapter(it)
    }.launchIn(viewLifecycleOwner.lifecycleScope)

    sideEffect.popularBooks.onEach {
      mBooksAdapter.submitList(it)
    }.launchIn(viewLifecycleOwner.lifecycleScope)
  }

  private fun onSwipedToRefresh(){
    mViewModel.onEvent(DashboardEvents.SwipedToRefresh)
  }

  private fun onShowCasePageChanged(position: Int) {
    mViewModel.onEvent(DashboardEvents.ScrolledShowCaseBooks(position))
  }

  private fun renderPopularBooksViewState(viewState: PopularBooksViewState) {
    when (viewState) {
      is PopularBooksViewState.PopularBooksSuccess -> renderPopularBooksSuccessViewState(viewState)
      is PopularBooksViewState.Loading -> setLoadingPopularBooks(true)
      is PopularBooksViewState.Empty -> setLoadingPopularBooks(false)
      is PopularBooksViewState.Failure -> setLoadingPopularBooks(false)
    }
  }

  private fun renderPopularBooksSuccessViewState(viewState: PopularBooksViewState.PopularBooksSuccess) {
    mBooksAdapter.submitList(viewState.popularBooks)
    setLoadingPopularBooks(false)
    setIsSwipeRefreshing(false)
  }

  private fun renderShowCaseViewState(viewState: ShowCaseViewState) {
    when (viewState) {
      is ShowCaseViewState.ShowCaseSuccess -> renderShowCaseSuccessViewState(viewState)
      is ShowCaseViewState.Loading -> setLoadingShowcaseBooks(true)
      is ShowCaseViewState.Empty -> setLoadingShowcaseBooks(false)
      is ShowCaseViewState.Failure -> setLoadingShowcaseBooks(false)
    }
  }

  private fun renderShowCaseSuccessViewState(viewState: ShowCaseViewState.ShowCaseSuccess) {
    initViewPagerAdapter(viewState.showcaseBooks)
    initFirstShowCaseUI(viewState.getFirstItem())
    setLoadingShowcaseBooks(false)
    setIsSwipeRefreshing(false)
  }


  private fun actionStateObserver(actionState: DashboardActionState) {
    when (actionState) {
      is DashboardActionState.ShowErrorMessageFromResource -> mPinToast.showErrorMessage(actionState.message)
    }
  }

  private fun initViewPagerAdapter(showCaseList: MutableList<ShowcaseBooks>) {
    mBinding.viewPagerShowCase.adapter = mDashboardShowCaseAdapter
    mDashboardShowCaseAdapter.currentItems = showCaseList
    mDashboardShowCaseAdapter.notifyDataSetChanged()
  }

  private fun initFirstShowCaseUI(showCase: ShowcaseBooks) {
    mBinding.textViewShowCaseBookName.textWithFadeAnimation = showCase.name
  }

  private fun initViewPagerPageTransformer() {
    val paddingPx = 300
    mBinding.viewPagerShowCase.setPadding(paddingPx, 0, paddingPx, 0)
    mBinding.viewPagerShowCase.setPageTransformer(false, ZoomFadeTransformer(paddingPx, 0.8f, 1f))
  }


  private fun setIsSwipeRefreshing(isRefreshing:Boolean){
    mBinding.root.isRefreshing = isRefreshing
  }

  private fun setLoadingPopularBooks(isLoading: Boolean) {
    mBinding.progressBarPopularBooks.isVisible = isLoading
  }

  private fun setLoadingShowcaseBooks(isLoading: Boolean) {
    mBinding.progressBarShowCase.isVisible = isLoading
  }

}