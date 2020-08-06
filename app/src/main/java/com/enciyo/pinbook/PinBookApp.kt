package com.enciyo.pinbook

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PinBookApp : Application(){


  override fun onCreate() {
    super.onCreate()
    MMKV.initialize(this)
  }

}