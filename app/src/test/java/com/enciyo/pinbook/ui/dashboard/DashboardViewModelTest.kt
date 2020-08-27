package com.enciyo.pinbook.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.usecases.FetchPopularBooksUseCase
import com.enciyo.pinbook.domain.usecases.FetchShowcaseBooksUseCase
import com.enciyo.pinbook.reducer.ReducerImp
import com.enciyo.pinbook.reducer.State
import com.enciyo.pinbook.testutils.MainCoroutineRule
import com.enciyo.pinbook.ui.dashboard.states.DashboardActionState
import com.enciyo.pinbook.ui.dashboard.states.DashboardEvents
import com.enciyo.pinbook.ui.dashboard.states.DashboardRepoState
import com.enciyo.pinbook.ui.dashboard.states.DashboardViewState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DashboardViewModelTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()


  @MockK
  lateinit var fetchPopularBooksUseCase: FetchPopularBooksUseCase

  @MockK
  lateinit var fetchShowcaseBooksUseCase: FetchShowcaseBooksUseCase

  private lateinit var reducer: ReducerImp<DashboardViewState,DashboardActionState,DashboardRepoState>

  lateinit var mViewModel: DashboardViewModel


  @Before
  fun onSetup() {
    MockKAnnotations.init(this)
    reducer = ReducerImp("DashboardViewModel",State.create())
    mViewModel = DashboardViewModel(
        mFetchPopularBooksUseCase = fetchPopularBooksUseCase,
        mFetchShowcaseBooksUseCase = fetchShowcaseBooksUseCase,
        mReducer = reducer
    )
  }



  @Test
  fun `GivenResultLoading_WhenLoginEventInit_ThenReturnViewLoadingState`() = runBlockPinBook{
    //Given

    every { fetchPopularBooksUseCase.invoke() } returns flow { emit(AwesomeResult.Loading(true)) }
    every { fetchShowcaseBooksUseCase.invoke() } returns flow { emit(AwesomeResult.Loading(true)) }

    //When

    mViewModel.onEvent(DashboardEvents.Init)

    //Then

    val expectedViewState = DashboardViewState(
        isLoadingPopularBooks = true,
        isLoadingShowcaseBooks = true
    )

    assert(reducer.viewState.value == expectedViewState)
    assert(reducer.actionState.value == null)
    assert(reducer.repoState.value?.popularBooks?.value.isNullOrEmpty())
    assert(reducer.repoState.value?.showcaseBooks?.value.isNullOrEmpty())

  }


  private fun runBlockPinBook(block: suspend TestCoroutineScope.() -> Unit) = mainCoroutineRule.testDispatcher.runBlockingTest {
    block.invoke(this)
  }

}