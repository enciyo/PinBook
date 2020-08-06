package com.enciyo.pinbook.utils

import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.transitionseverywhere.extra.Scale


fun View.visibilityWithScaleAnimation(isVisible:Boolean, transitionsContainer:ViewGroup){
  val set: TransitionSet = TransitionSet()
      .addTransition(Scale(0.1f))
      .setDuration(230)
      .addTransition(Fade()).apply {
        if (isVisible) interpolator = LinearOutSlowInInterpolator()
        else interpolator = FastOutLinearInInterpolator()
      }
  TransitionManager.beginDelayedTransition(transitionsContainer,set)
  this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}