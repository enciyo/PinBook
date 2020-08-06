package com.enciyo.pinbook.ui.login

import com.enciyo.pinbook.reducer.UserInteractions


sealed class LoginUserIneractions : UserInteractions() {
  object LoginClicked : LoginUserIneractions()
  object RegisterClicked : LoginUserIneractions()
  data class InputUsernameTextChanged(val changedText: String) : LoginUserIneractions()
  data class InputPasswordTextChanged(val changedText: String) : LoginUserIneractions()
}