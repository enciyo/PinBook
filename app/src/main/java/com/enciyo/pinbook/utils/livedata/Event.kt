package com.enciyo.pinbook.utils.livedata

import androidx.lifecycle.MutableLiveData


open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
      return when(hasBeenHandled){
        true-> null
        false -> {
          hasBeenHandled = true
          content
        }
      }
    }

    fun peekContent(): T = content
}

fun <T> MutableLiveData<Event<T>>.postEvent(data: T) = this.postValue(Event(data))