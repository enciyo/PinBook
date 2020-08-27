package com.enciyo.pinbook.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.data.remote.PinBookService
import com.enciyo.pinbook.domain.usecases.RegisterUseCase
import com.enciyo.pinbook.reducer.ReducerImp
import com.enciyo.pinbook.reducer.State
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.livedata.postEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@ExperimentalCoroutinesApi
class RegisterViewModel @ViewModelInject constructor(
    private val mPinBookService: PinBookService,
    private val mRegisterUseCase: RegisterUseCase
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


    val mReducer = ReducerImp<RegisterViewState, RegisterActionState, RegisterRepoState>(
        "Register",
        State.create()
    )


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
                mReducer.viewTo(
                    mReducer.currentViewState().copy(isFailure = true, isLoading = false)
                )
                    .actionTo(RegisterActionState.ShowResourceError(isValid.id))
            }
            is RegisterFormValidation.Status.Success -> fetchRegister()
        }
    }


    private fun onLoginButtonClicked() {
        mReducer.actionTo(RegisterActionState.NavigateToLogin)
    }


    private fun fetchRegister() {
        mRegisterUseCase
            .invoke(
                RegisterUseCase.RegisterUseCaseParams(
                    mRegisterFormValidation.username.toString(),
                    mRegisterFormValidation.password.toString(),
                    mRegisterFormValidation.displayName.toString(),
                    mRegisterFormValidation.email.toString()
                )
            )
            .onEach {
                when (it) {
                    is AwesomeResult.Success -> {
                        mReducer.viewTo(mReducer.currentViewState().copy(isSuccess = true, isLoading = false))
                            .actionTo(RegisterActionState.NavigateToLogin)
                    }
                    is AwesomeResult.Loading -> mReducer.viewTo(
                        mReducer.currentViewState().copy(isLoading = true)
                    )
                    is AwesomeResult.UnknownError -> {
                        mReducer
                            .viewTo(mReducer.currentViewState().copy(isFailure = true, isLoading = false))
                            .actionTo(RegisterActionState.ShowError(it.message))
                    }
                    is AwesomeResult.ServerError -> {
                        mReducer
                            .viewTo(mReducer.currentViewState().copy(isFailure = true, isLoading = false))
                            .actionTo(RegisterActionState.ShowError(it.errorData?.message.toString()))
                    }
                    else -> {
                        mReducer
                            .viewTo(mReducer.currentViewState().copy(isFailure = true, isLoading = false))
                            .actionTo(RegisterActionState.ShowError("Error"))
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    fun renderedRegisterSuccess() {
        _actionState.postEvent(RegisterActionState.NavigateToLogin)
    }


}