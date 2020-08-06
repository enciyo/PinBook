package com.enciyo.library.livedata

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@PublishedApi
internal fun <T> LiveData<Event<T>>.eventObserve(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
  this.observe(lifecycleOwner, Observer {
    it.getContentIfNotHandled()?.let(block)
  })
}


@ExperimentalCoroutinesApi
@PublishedApi
internal fun <T> StateFlow<Event<T>>.eventObserve(lifecycleOwner: Fragment, block: (T) -> Unit) {
  this.onEach {
    it.getContentIfNotHandled()?.let(block)
  }.launchIn(lifecycleOwner.viewLifecycleOwner.lifecycleScope)
}


@ExperimentalCoroutinesApi
@PublishedApi
internal fun <T> StateFlow<Event<T>?>.eventObserve(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
  this.onEach {
    it?.getContentIfNotHandled()?.let(block)
  }.launchIn(lifecycleOwner.lifecycleScope)
}

