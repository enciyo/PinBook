package com.enciyo.pinbook.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.google.android.material.textfield.TextInputLayout
import kotlin.reflect.KFunction1


fun View?.setOnClickListener(block: () -> Unit) {
  this?.setOnClickListener {
    block.invoke()
  }
}

var TextView.textWithFadeAnimation: CharSequence?
  get() = this.text
  set(value) {
    if (this.text.isNullOrBlank().not())
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
    else
      this@textWithFadeAnimation.text = value
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



fun Fragment.hideKeyboard() {
  view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
  hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
  val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}