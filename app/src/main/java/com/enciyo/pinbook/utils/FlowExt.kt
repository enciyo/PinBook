package com.enciyo.pinbook.utils

import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KFunction1


fun <T> Flow<T>.onEach(action: KFunction1<T, Unit>): Flow<T> {
  return this.onEach {
    action.invoke(it)
  }
}

fun <T> Flow<Event<T>>.onEventEach(action: KFunction1<T, Unit>): Flow<Event<T>> {
  return this.onEach {
    it.getContentIfNotHandled()?.let(action)
  }
}

x