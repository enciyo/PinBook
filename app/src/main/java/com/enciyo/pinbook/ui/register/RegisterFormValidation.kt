package com.enciyo.pinbook.ui.register

import androidx.annotation.StringRes
import com.enciyo.pinbook.R


data class RegisterFormValidation(
    var username: String? = null,
    var password: String? = null,
    var displayName: String? = null,
    var email: String? = null
) {
  val isValid
    get() = when {
      username.isNullOrBlank()
          || password.isNullOrBlank()
          || displayName.isNullOrBlank()
          || email.isNullOrBlank() -> Status.NotValid(R.string.RegisterValidationError)
      else -> Status.Success
    }


  sealed class Status {
    data class NotValid(@StringRes val id: Int) : Status()
    object Success : Status()
  }

}