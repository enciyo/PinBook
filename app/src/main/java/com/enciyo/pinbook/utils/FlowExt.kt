package com.enciyo.pinbook.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KFunction1


fun <T> Flow<T>.onEach(action: KFunction1<T, Unit>): Flow<T> {
  return this.onEach {
    action.invoke(it)
  }
}
