package com.enciyo.pinbook.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.enciyo.pinbook.R
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

    private lateinit var reducer: ReducerImp<DashboardViewState, DashboardActionState, DashboardRepoState>

    lateinit var mViewModel: DashboardViewModel


    @Before
    fun onSetup() {
        MockKAnnotations.init(this)
        reducer = ReducerImp("DashboardViewModel", State.create())
        mViewModel = DashboardViewModel(
            mFetchPopularBooksUseCase = fetchPopularBooksUseCase,
            mFetchShowcaseBooksUseCase = fetchShowcaseBooksUseCase,
            mReducer = reducer
        )
    }


    @Test
    fun `GivenResultLoading_WhenLoginEventInit_ThenReturnLoadingState`() = runBlockPinBook {
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

        assert(reducer.currentViewState() == expectedViewState)
        assert(reducer.currentActionState() == null)
        assert(reducer.currentRepoState()?.popularBooks == null)
        assert(reducer.currentRepoState()?.showcaseBooks == null)

    }

    @Test
    fun `GivenResultShowcaseError_WhenLoginEventInit_ThenReturnShowcaseError`() =
        runBlockPinBook {
            val throwable = IllegalStateException("State Exception")
            every { fetchPopularBooksUseCase.invoke() } returns flow {
                emit(
                    AwesomeResult.Loading(
                        true
                    )
                )
            }
            every { fetchShowcaseBooksUseCase.invoke() } returns flow {
                emit(
                    AwesomeResult.UnknownError(
                        throwable
                    )
                )
            }

            //When
            mViewModel.onEvent(DashboardEvents.Init)

            //Than
            val expectedViewState = DashboardViewState(
                isLoadingPopularBooks = true,
                isLoadingShowcaseBooks = false,
                isEmptyShowcaseBooks = true
            )
            val expectedAction =
                DashboardActionState.ShowErrorMessageFromResource(R.string.UnknowError)

            assert(reducer.currentViewState() == expectedViewState) {
                "Actual is ${reducer.currentViewState()}"
            }
            assert(reducer.currentActionState() == expectedAction) {
                "Actual Action is ${reducer.currentActionState()}"
            }

            assert(reducer.currentRepoState()?.showcaseBooks?.data == null)

        }

    @Test
    fun `GivenResultPopularBooksError_WhenLoginEventInit_ThenReturnPopularBooksError`() =
        runBlockPinBook {
            val throwable = IllegalStateException()
            every { fetchPopularBooksUseCase.invoke() } returns flow {
                emit(
                    AwesomeResult.UnknownError(
                        throwable
                    )
                )
            }
            every { fetchShowcaseBooksUseCase.invoke() } returns flow {
                emit(
                    AwesomeResult.Loading(
                        true
                    )
                )
            }

            //When
            mViewModel.onEvent(DashboardEvents.Init)

            //Than
            val expectedViewState = DashboardViewState(
                isLoadingShowcaseBooks = true,
                isLoadingPopularBooks = false,
                isEmptyPopularBooks = true
            )

            val expectedActionState =
                DashboardActionState.ShowErrorMessageFromResource(R.string.UnknowError)

            assert(reducer.currentViewState() == expectedViewState){
                """
                    Actual ViewState is ${reducer.currentViewState()}
                """.trimIndent()
            }
            assert(reducer.currentActionState() == expectedActionState){
                """
                    Actual Action is ${reducer.currentActionState()}
                """.trimIndent()
            }
            assert(reducer.currentRepoState()?.popularBooks?.value == null)
        }



    private fun runBlockPinBook(block: suspend TestCoroutineScope.() -> Unit) =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            block.invoke(this)
        }

}