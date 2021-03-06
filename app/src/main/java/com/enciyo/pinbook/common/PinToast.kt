package com.enciyo.pinbook.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.livedata.postEvent


class PinToast(private val applicationContext: Context) {

    private val mToastMessage: MutableLiveData<Event<ToastType>> = MutableLiveData()
    val toastMessage: LiveData<Event<ToastType>>
        get() = mToastMessage


    fun showErrorMessage(message: String) {
        mToastMessage.postEvent(ToastType.ErrorMessage(message))
    }

    fun showErrorMessage(@StringRes message: Int) {
        mToastMessage.postEvent(ToastType.ErrorMessage(applicationContext.getString(message)))
    }

    fun showSuccessMessage(message: String) {
        mToastMessage.postEvent(ToastType.SuccessMessage(message))
    }


    sealed class ToastType {
        data class SuccessMessage(val message: String) : ToastType()
        data class ErrorMessage(val message: String) : ToastType()
    }


}