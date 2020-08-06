package com.enciyo.pinbook.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.network.NetworkConnectivityManager
import com.enciyo.pinbook.common.validations.FormValidation
import com.enciyo.pinbook.domain.model.GeneralError
import com.enciyo.pinbook.domain.model.User
import com.enciyo.pinbook.domain.usecases.LoginUseCase
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.ReducerImp
import com.enciyo.pinbook.reducer.State
import com.enciyo.pinbook.testutils.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations


@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()


  private val testDispatcher
    get() = mainCoroutineRule.testDispatcher

  //Mock
  private var mLoginUseCase: LoginUseCase = mockk()
  private var mFormValidation: FormValidation = mockk(relaxed = true)
  private var mNetworkConnectivityManager: NetworkConnectivityManager = mockk()


  //Objects
  private val mReducer: Reducer<LoginViewState, LoginActionState, LoginSideEffect>
    get() = ReducerImp("", State.create())

  private val mLoginViewModel: LoginViewModel
    get() = LoginViewModel(mLoginUseCase, mNetworkConnectivityManager, mFormValidation, mReducer)


  @Before
  fun setup() {
    MockKAnnotations.init(this)
    MockitoAnnotations.initMocks(this)
  }


  @After
  fun stop() {
    testDispatcher.cancel()
  }


  @Test
  fun `GivenResultUnknownError_WhenLoginEventCalled_ThenReturnFailureView'UnknownErrorAction`() = runBlockPinBook {
    with(mLoginViewModel) {

      //Given
      val throwable = Throwable("Test")
      every { mNetworkConnectivityManager.isNetworkAvailable } returns true
      every { mFormValidation.isValid } returns true
      every { mLoginUseCase.invoke(any()) } returns flow { emit(AwesomeResult.UnknownError(throwable)) }

      //When
      onEvent(LoginUserIneractions.LoginClicked)

      //Than
      val lastState = state.lastState()

      val expectedView = LoginViewState(isFailure = true)
      val expectedAction = LoginActionState.ShowUnknownErrorMessage(R.string.UnknowError)

      lastState.assertViewState(expectedView)
      lastState.assertAction(expectedAction)
      lastState.assertSideEffect(null)

    }
  }


  @Test
  fun `GivenResultSuccess_WhenLoginEventCalled_ThenReturnSuccessView'NavigateToMainAction`() = runBlockPinBook {
    with(mLoginViewModel) {

      // Given
      every { mNetworkConnectivityManager.isNetworkAvailable } returns true
      every { mFormValidation.isValid } returns true
      every { mLoginUseCase.invoke(any()) } returns flow { emit(AwesomeResult.Success(LoginUseCase.LoginUseCaseResult(User()), true)) }

      //When
      onEvent(LoginUserIneractions.LoginClicked)

      //Then
      val lastState = state.lastState()

      val expectedView = LoginViewState(isSuccess = true)
      val expectedAction = LoginActionState.NavigateToMain

      lastState.assertViewState(expectedView)
      lastState.assertAction(expectedAction)
      lastState.assertSideEffect(null)

    }
  }


  @Test
  fun `GivenResultFailure_WhenLoginEventCalled_ThenReturnFailureView'ShowApiErrorMessageAction`() = runBlockPinBook {
    with(mLoginViewModel) {

      // Given
      val generalError = GeneralError("Test", "Test", 400)
      every { mNetworkConnectivityManager.isNetworkAvailable } returns true
      every { mFormValidation.isValid } returns true
      every { mLoginUseCase.invoke(any()) } returns flow { emit(AwesomeResult.ServerError(generalError)) }

      //When
      onEvent(LoginUserIneractions.LoginClicked)

      //Then
      val lastState = state.lastState()

      val expectedView = LoginViewState(isFailure = true)
      val expectedAction = LoginActionState.ShowApiErrorMessage(generalError.message)

      lastState.assertViewState(expectedView)
      lastState.assertAction(expectedAction)
      lastState.assertSideEffect(null)

    }
  }


  @Test
  fun `GivenResultLoading_WhenLoginEventCalled_ThenReturnLoading`() = runBlockPinBook {
    with(mLoginViewModel) {

      // Given
      every { mNetworkConnectivityManager.isNetworkAvailable } returns true
      every { mFormValidation.isValid } returns true
      every { mLoginUseCase.invoke(any()) } returns flow { emit(AwesomeResult.Loading(true)) }

      //When
      onEvent(LoginUserIneractions.LoginClicked)

      //Then
      val lastState = state.lastState()

      val expectedView = LoginViewState(isLoading = true)

      lastState.assertViewState(expectedView)
      lastState.assertAction(null)
      lastState.assertSideEffect(null)

    }
  }

  @Test
  fun `GivenResultNoConnectionNetwork_WhenLoginEventCalled_ThenReturnFailureView'ShowUnknownErrorMessage`() = runBlockPinBook {
    with(mLoginViewModel) {

      // Given
      every { mNetworkConnectivityManager.isNetworkAvailable } returns true
      every { mFormValidation.isValid } returns true
      every { mLoginUseCase.invoke(any()) } returns flow { emit(AwesomeResult.NoNetworkConnection) }

      //When
      onEvent(LoginUserIneractions.LoginClicked)

      //Then
      val lastState = state.lastState()

      val expectedView = LoginViewState(isFailure = true)
      val expectedAction = LoginActionState.ShowUnknownErrorMessage(R.string.NoConnection)


      lastState.assertViewState(expectedView)
      lastState.assertAction(expectedAction)
      lastState.assertSideEffect(null)

    }
  }




  private fun runBlockPinBook(block: suspend TestCoroutineScope.() -> Unit) = testDispatcher.runBlockingTest {
    block.invoke(this)
  }

}



