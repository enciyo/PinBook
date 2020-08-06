package com.enciyo.pinbook.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.livedata.postEvent


class PinToast(private val applicationContext: Context) {

  private val _toastMessage: MutableLiveData<Event<ToastType>> = MutableLiveData()
  val toastMessage: LiveData<Event<ToastType>>
    get() = _toastMessage


  fun showErrorMessage(message:String)  {
    _toastMessage.postEvent(ToastType.ErrorMessage(message))
  }

  fun showErrorMessage(@StringRes message:Int){
    _toastMessage.postEvent(ToastType.ErrorMessage(applicationContext.getString(message)))
  }

  fun showSuccessMessage(message: String){
    _toastMessage.postEvent(ToastType.SuccessMessage(message))
  }
  fun showSuccessMessage(@StringRes message: Int){
    _toastMessage.postEvent(ToastType.SuccessMessage(applicationContext.getString(message)))
  }



  sealed class ToastType{
    data class SuccessMessage(val message: String):ToastType()
    data class ErrorMessage(val message: String): ToastType()
  }


}