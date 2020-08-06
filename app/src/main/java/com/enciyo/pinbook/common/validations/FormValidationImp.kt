package com.enciyo.pinbook.common.validations

import androidx.annotation.MainThread
import com.enciyo.pinbook.common.CoScope
import com.enciyo.pinbook.common.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@ExperimentalCoroutinesApi
class FormValidationImp @Inject constructor(
    @CoScope val coroutineScope: CoroutineScope,
    @DefaultDispatcher val coroutineDispatcher: CoroutineDispatcher
) : FormValidation {


  private val _validationResult: MutableStateFlow<FormValidation.ValidationModel> = MutableStateFlow(FormValidation.ValidationModel("", FormValidation.ValidationType.Init))
  override val validationFlow: MutableStateFlow<FormValidation.ValidationModel>
    get() = _validationResult

  private val mValidationModels = mutableListOf<FormValidation.ValidationModel>()

  override val isValid: Boolean
    get() = mValidationModels.find { it.isValid.not() } == null

  override fun checkAndValidateAll() {
    mValidationModels.forEach(::setValid)
  }

  @MainThread
  override fun clear() {
    mValidationModels.clear()
    coroutineScope.cancel()
  }


  override fun addValidation(model: FormValidation.ValidationModel) = apply {
    if (mValidationModels.find { it.validationType == model.validationType } == null) {
      mValidationModels.add(model)
    }
  }

  override fun validator(model: FormValidation.ValidationModel, changeText: String) {
    model.text = changeText
    setValid(model)
  }

  private fun setValid(model: FormValidation.ValidationModel) {
    val isValid = model.validationType.mustBe.invoke(model.text)
    model.isValid = isValid
    _validationResult.value = model.copy()
  }




}
