package com.enciyo.pinbook.common.validations

import com.enciyo.pinbook.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow


@ExperimentalCoroutinesApi
interface FormValidation {

  val isValid: Boolean

  val validationFlow: MutableStateFlow<ValidationModel>

  fun checkAllIsValidate()

  fun clear()

  fun addValidation(model: ValidationModel): FormValidation

  fun validator(model: ValidationModel, changeText: String)

  sealed class ValidationType(
      val mustBe: (String) -> Boolean, val resourceMessage: Int
  ) {
    object Init : ValidationType({ it.isEmpty() }, R.string.msg_no_alert_showing)
    object Username : ValidationType({ it.length > 3 }, R.string.ValidationErrorUsername)
    object Password : ValidationType({ it.length > 6 }, R.string.ValidationErrorPassword)
  }

  data class ValidationModel(
      var text: String,
      val validationType: ValidationType,
      var isValid: Boolean = false
  )

}