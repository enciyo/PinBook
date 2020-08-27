package com.enciyo.pinbook.utils.viewpager

import android.view.View
import androidx.viewpager.widget.ViewPager


class ZoomFadeTransformer(private val paddingPx: Int, private val MIN_SCALE: Float, private val MAX_SCALE: Float) : ViewPager.PageTransformer {
  override fun transformPage(page: View, position: Float) {
    val pagerWidthPx = (page.parent as ViewPager).width.toFloat()
    val pageWidthPx: Float = pagerWidthPx - 2 * paddingPx
    val maxVisiblePages = pagerWidthPx / pageWidthPx
    val center = maxVisiblePages / 2f
    val scale: Float
    scale = if (position + 0.5f < center - 0.5f || position > center) {
      MIN_SCALE
    } else {
      val coef: Float = if (position + 0.5f < center) {
        (position + 1 - center) / 0.5f
      } else {
        (center - position) / 0.5f
      }
      coef * (MAX_SCALE - MIN_SCALE) + MIN_SCALE
    }
    page.scaleX = scale
    page.scaleY = scale
  }
}