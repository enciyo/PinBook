package com.enciyo.pinbook.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@ExperimentalCoroutinesApi
class RepoWrapper<T>(val data: T) {
  private val flow: MutableStateFlow<T> = MutableStateFlow(data)

  var value
    get() = flow.value
    set(value) {
      flow.value = value
    }

  fun asFlow() = (flow as StateFlow<T>)

}
