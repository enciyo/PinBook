package com.enciyo.pinbook.ui.register


sealed class RegisterViewEvents {
  object RegisterClicked: RegisterViewEvents()
  object LoginClicked : RegisterViewEvents()
  data class InputUsernameTextChanged(val changedText:String): RegisterViewEvents()
  data class InputDisplayNameTextChanged(val changedText:String): RegisterViewEvents()
  data class InputEmailTextChanged(val changedText:String): RegisterViewEvents()
  data class InputPasswordTextChanged(val changedText:String): RegisterViewEvents()

}