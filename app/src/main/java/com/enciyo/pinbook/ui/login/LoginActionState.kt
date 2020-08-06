package com.enciyo.pinbook.ui.login

import androidx.lifecycle.MutableLiveData
import com.enciyo.pinbook.domain.model.User
import com.enciyo.pinbook.reducer.ActionState
import com.enciyo.pinbook.reducer.SideEffect


sealed class LoginActionState : ActionState() {
  object NavigateToMain : LoginActionState()
  object NavigateToRegister : LoginActionState()
  data class ShowWelcomeMessage(val message: String) : LoginActionState()
  data class ShowApiErrorMessage(val message: String) : LoginActionState()
  data class ShowUnknownErrorMessage(val message: Int) : LoginActionState()
  object ShowPopUp : LoginActionState()
}




