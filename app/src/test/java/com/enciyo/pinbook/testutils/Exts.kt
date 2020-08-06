package com.enciyo.pinbook.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.enciyo.pinbook.utils.livedata.Event
import io.mockk.CapturingSlot
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

inline fun <reified T> CapturingSlot<Event<T>>.value() = this.captured.getContentIfNotHandled()!!

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
  var data: T? = null
  val latch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(o: T?) {
      data = o
      latch.countDown()
      this@getOrAwaitValue.removeObserver(this)
    }
  }

  this.observeForever(observer)

  // Don't wait indefinitely if the LiveData is not set.
  if (!latch.await(time, timeUnit)) {
    throw TimeoutException("LiveData value was never set.")
  }

  @Suppress("UNCHECKED_CAST")
  return data as T
}