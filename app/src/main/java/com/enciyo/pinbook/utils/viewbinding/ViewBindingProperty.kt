package com.enciyo.pinbook.utils.viewbinding

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


abstract class ViewBindingProperty<in R, T : ViewBinding>(
    private val viewBinder: (R) -> T
) : ReadOnlyProperty<R, T> {

  internal var viewBinding: T? = null
  private val lifecycleObserver = BindingLifecycleObserver()

  protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

  @MainThread
  override fun getValue(thisRef: R, property: KProperty<*>): T {
    checkIsMainThread()
    viewBinding?.let { return it }

    getLifecycleOwner(thisRef).lifecycle.addObserver(lifecycleObserver)
    return viewBinder(thisRef).also { viewBinding = it }
  }

  private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

    private val mainHandler = Handler(Looper.getMainLooper())

    @MainThread
    override fun onDestroy(owner: LifecycleOwner) {
      owner.lifecycle.removeObserver(this)
      mainHandler.post {
        viewBinding = null
      }
    }
  }
}


@get:RestrictTo(RestrictTo.Scope.LIBRARY)
internal inline val isMainThread: Boolean
  get() = Looper.myLooper() == Looper.getMainLooper()

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun checkIsMainThread() = check(isMainThread)

