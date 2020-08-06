package com.enciyo.pinbook.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.data.remote.PinBookService
import com.enciyo.pinbook.data.remote.model.ResponseUser
import com.enciyo.pinbook.reducer.ReducerImp
import com.enciyo.pinbook.reducer.State
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.livedata.postEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch




@ExperimentalCoroutinesApi
class RegisterViewModel @ViewModelInject constructor(
    private val mPinBookService: PinBookService
) : ViewModel() {

  var username: String? = null
  var displayName: String? = null
  var email: String? = null
  var password: String? = null


  

  private val mRegisterFormValidation = RegisterFormValidation()

  private val _viewState: MutableLiveData<Event<RegisterViewState>> = MutableLiveData()
  val viewState: LiveData<Event<RegisterViewState>>
    get() = _viewState

  private val _actionState: MutableLiveData<Event<RegisterActionState>> = MutableLiveData()
  val actionState: LiveData<Event<RegisterActionState>>
    get() = _actionState

  val mReducer = ReducerImp<RegisterViewState, RegisterActionState, RegisterSideEffect>("Register", State.create())


  fun onEvent(event: RegisterViewEvents) {
    when (event) {
      is RegisterViewEvents.RegisterClicked -> onRegisterClicked()
      is RegisterViewEvents.LoginClicked -> onLoginButtonClicked()
      is RegisterViewEvents.InputUsernameTextChanged -> mRegisterFormValidation.username = event.changedText
      is RegisterViewEvents.InputDisplayNameTextChanged -> mRegisterFormValidation.displayName = event.changedText
      is RegisterViewEvents.InputEmailTextChanged -> mRegisterFormValidation.email = event.changedText
      is RegisterViewEvents.InputPasswordTextChanged -> mRegisterFormValidation.password = event.changedText
    }
  }

  private fun dispatch(actionState: RegisterActionState) {
    mReducer.actionTo(actionState)
  }

  private fun onRegisterClicked() {
    when (val isValid = mRegisterFormValidation.isValid) {
      is RegisterFormValidation.Status.NotValid -> {
        mReducer.moveTo(mReducer.currentViewState().copy(isFailure = true,isLoading = false))
                .actionTo(RegisterActionState.ShowResourceError(isValid.id))
      }
      is RegisterFormValidation.Status.Success -> fetchRegister()
    }
  }


  private fun onLoginButtonClicked() {
    mReducer.actionTo(RegisterActionState.NavigateToLogin)
  }


  private fun fetchRegister() {
    mReducer.moveTo(mReducer.currentViewState().copy(isLoading = true))
    viewModelScope.launch {
      val response = try {
        mPinBookService.register(ResponseUser(
            mDisplayName = displayName,
            mEmail = email,
            mPassword = password,
            mUsername = username
        ))
      } catch (e: Exception) {
        mReducer
            .moveTo(mReducer.currentViewState().copy(isFailure = true, isLoading = false))
            .actionTo(RegisterActionState.ShowError(e.message.toString()))
        null
      }
      if (response != null) {
        mReducer.moveTo(mReducer.currentViewState().copy(isSuccess = true, isLoading = false))
      }
    }
  }

  fun renderedRegisterSuccess() {
    _actionState.postEvent(RegisterActionState.NavigateToLogin)
  }


}