package com.enciyo.pinbook.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.network.NetworkConnectivityManager
import com.enciyo.pinbook.common.validations.FormValidation
import com.enciyo.pinbook.domain.usecases.LoginUseCase
import com.enciyo.pinbook.reducer.BaseViewModel
import com.enciyo.pinbook.reducer.Reducer
import com.enciyo.pinbook.reducer.Redux
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn


@ExperimentalCoroutinesApi
class LoginViewModel @ViewModelInject constructor(
    private val mLoginUseCase: LoginUseCase,
    private val mNetworkConnectivityManager: NetworkConnectivityManager,
    private val mFormValidation: FormValidation,
    @Redux val reducer: Reducer<LoginViewState, LoginActionState, LoginRepoState>
) : BaseViewModel<LoginViewState, LoginActionState, LoginRepoState, LoginUserIneractions>(reducer) {


  private val mUsernameFormValidation = FormValidation.ValidationModel("", FormValidation.ValidationType.Username)
  private val mPasswordFormValidation = FormValidation.ValidationModel("", FormValidation.ValidationType.Password)


  init {
    initFormValidation()
  }

  override fun onEvent(event: LoginUserIneractions) {
    when (event) {
      is LoginUserIneractions.LoginClicked -> onLoginClicked()
      is LoginUserIneractions.RegisterClicked -> actionTo(LoginActionState.NavigateToRegister)
      is LoginUserIneractions.InputUsernameTextChanged -> mFormValidation.validator(mUsernameFormValidation, event.changedText)
      is LoginUserIneractions.InputPasswordTextChanged -> mFormValidation.validator(mPasswordFormValidation, event.changedText)
    }
  }

  private fun initFormValidation() {
    mFormValidation
        .addValidation(mUsernameFormValidation)
        .addValidation(mPasswordFormValidation)

    mFormValidation.validationFlow
        .onEach(::handleValidationResult)
        .launchIn(viewModelScope)
  }

  private fun handleValidationResult(validationImp: FormValidation.ValidationModel) {
    when (validationImp.validationType) {
      is FormValidation.ValidationType.Username -> viewTo(checkUsernameState(validationImp))
      is FormValidation.ValidationType.Password -> viewTo(checkPasswordState(validationImp))
    }
  }

  private fun checkUsernameState(validationImp: FormValidation.ValidationModel): LoginViewState =
      currentViewState().copy(isUsernameError = validationImp.isValid.not())

  private fun checkPasswordState(validationImp: FormValidation.ValidationModel): LoginViewState =
      currentViewState().copy(isPasswordError = validationImp.isValid.not())

  private fun onLoginClicked() {
    when {
      mNetworkConnectivityManager.isNetworkAvailable.not() -> onConnectionError()
      mFormValidation.isValid.not() -> mFormValidation.checkAllIsValidate()
      mFormValidation.isValid -> fetchLogin()
    }
  }


  private fun fetchLogin() {
    mLoginUseCase.invoke(LoginUseCase.LoginUseCaseParameters(mUsernameFormValidation.text, mPasswordFormValidation.text))
        .onEach(::onHandleLoginResult)
        .launchIn(viewModelScope)
  }

  private fun onHandleLoginResult(awesomeResult: AwesomeResult<LoginUseCase.LoginUseCaseResult>) {
    when (awesomeResult) {
      is AwesomeResult.Success -> onSuccess(awesomeResult)
      is AwesomeResult.Loading -> viewTo(currentViewState().copy(isLoading = true))
      is AwesomeResult.ServerError -> onServerError(awesomeResult)
      is AwesomeResult.UnknownError -> onUnknownError(awesomeResult)
      is AwesomeResult.NoNetworkConnection -> onConnectionError()
    }
  }

  private fun onSuccess(awesomeResult: AwesomeResult.Success<LoginUseCase.LoginUseCaseResult>) {
    viewTo(currentViewState().copy(isSuccess = true, isLoading = false, isFailure = false))
        .actionTo(LoginActionState.NavigateToMain)
  }

  private fun onServerError(awesomeResult: AwesomeResult.ServerError) {
    viewTo(currentViewState().copy(isFailure = true, isLoading = false))
        .actionTo(LoginActionState.ShowApiErrorMessage(awesomeResult.errorData?.message.toString()))
  }

  private fun onUnknownError(awesomeResult: AwesomeResult.UnknownError) {
    viewTo(currentViewState().copy(isFailure = true))
        .actionTo(LoginActionState.ShowUnknownErrorMessage(R.string.UnknowError))
  }

  private fun onConnectionError() {
    viewTo(currentViewState().copy(isFailure = true))
        .actionTo(LoginActionState.ShowUnknownErrorMessage(R.string.NoConnection))
  }


  override fun onCleared() {
    mFormValidation.clear()
    super.onCleared()
  }


}




