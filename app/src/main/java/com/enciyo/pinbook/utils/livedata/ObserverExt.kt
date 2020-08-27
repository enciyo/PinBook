package com.enciyo.pinbook.utils.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


@PublishedApi
internal fun <T> LiveData<Event<T>>.eventObserve(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
  this.observe(lifecycleOwner, Observer {
    it.getContentIfNotHandled()?.let(block)
  })
}
