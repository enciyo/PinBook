package com.enciyo.pinbook.ui.login

import com.enciyo.pinbook.reducer.ActionState


sealed class LoginActionState : ActionState() {
  object NavigateToMain : LoginActionState()
  object NavigateToRegister : LoginActionState()
  data class ShowWelcomeMessage(val message: String) : LoginActionState()
  data class ShowApiErrorMessage(val message: String) : LoginActionState()
  data class ShowUnknownErrorMessage(val message: Int) : LoginActionState()
  object ShowPopUp : LoginActionState()
}




