package com.enciyo.pinbook.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KMutableProperty0


fun EditText?.addTextChangedListener(kMutableProperty0: KMutableProperty0<String?>) {
  this?.addTextChangedListener {
    kMutableProperty0.set(it.toString())
  }
}

fun View?.setOnClickListener(block: () -> Unit) {
  this?.setOnClickListener {
    block.invoke()
  }
}

var TextView.textWithFadeAnimation: CharSequence?
  get() = this.text
  set(value) {
    AlphaAnimation(0.3f, 1f).also {
      it.duration = 500
      it.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) = Unit
        override fun onAnimationEnd(animation: Animation?) = Unit
        override fun onAnimationStart(animation: Animation?) {
          this@textWithFadeAnimation.text = value
        }
      })
      this.startAnimation(it)
    }
  }

fun ViewPager.addOnPageChangeListener(
    scrollStateChanged: ((Int) -> Unit)? = null,
    pageScrolled: ((Int, Float, Int) -> Unit)? = null,
    pageSelected: ((Int) -> Unit)
) {
  this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
      scrollStateChanged?.invoke(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      pageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
      pageSelected?.invoke(position)
    }
  })
}

fun ViewPager.addOnPageChangeListener(kFunction1: KFunction1<Int, Unit>) {
  this.addOnPageChangeListener {
    kFunction1.invoke(it)
  }
}


fun EditText?.addTextChangedListener(kFunction1: KFunction1<String, Unit>) {
  this?.addTextChangedListener {
    kFunction1.invoke(it.toString())
  }
}

fun EditText?.addTextChangedListener(kFunction1: KFunction1<String, Unit>, viewLifecycle: LifecycleOwner) {
  viewLifecycle.lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
      super.onResume(owner)
      this@addTextChangedListener?.addTextChangedListener {
        kFunction1.invoke(it.toString())
      }
    }

    override fun onDestroy(owner: LifecycleOwner) {
      this@addTextChangedListener?.addTextChangedListener(null)
      super.onDestroy(owner)
    }
  })
}


fun TextInputLayout.safeErrorMessage(errorMess: Int?) {
  if (errorMess != null && this.error.isNullOrBlank()) {
    this.error = errorMess?.let(context::getString)
  } else if (errorMess == null) this.error = null
}


val ViewGroup.inflater
  get() = LayoutInflater.from(this.context)

private fun View.clicks(action: KFunction0<Unit>) = callbackFlow<Unit> {
  this@clicks.setOnClickListener {
    this.offer(Unit)
  }
  awaitClose { this@clicks.setOnClickListener(null) }
}.onEach { action.invoke() }



fun View.click(
    throttleFirst: Long = 1500,
    debounce: Long = 1,
    viewLifecycle: LifecycleOwner,
    action: KFunction0<Unit>
) {
  this.clicks(action)
      .throttleFirst(throttleFirst)
      .debounce(debounce)
      .onEach { action.invoke() }
      .launchIn(viewLifecycle.lifecycleScope)
}







fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> {
  var job: Job = Job().apply { complete() }
  return onCompletion { job.cancel() }.run {
    flow {
      coroutineScope {
        collect { value ->
          if (!job.isActive) {
            emit(value)
            job = launch { delay(windowDuration) }
          }
        }
      }
    }
  }
}