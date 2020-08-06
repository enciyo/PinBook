package com.enciyo.pinbook.ui.login

import com.enciyo.pinbook.reducer.ViewState


data class LoginViewState(
    var isSuccess: Boolean = false,
    var isLoading: Boolean = false,
    var isFailure: Boolean = false,
    var isUsernameError: Boolean = false,
    var isPasswordError: Boolean = false
) : ViewState() {

  val isButtonLoginVisible
    get() = isLoading.not()

  val isInputUsernameEnable
    get() = isLoading.not()

  val isInputPasswordEnable
    get() = isLoading.not()

}




