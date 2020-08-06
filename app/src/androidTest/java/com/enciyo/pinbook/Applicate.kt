package com.enciyo.pinbook

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.tencent.mmkv.MMKV
import dagger.hilt.android.internal.testing.TestApplicationComponentManager
import dagger.hilt.android.internal.testing.TestApplicationComponentManagerHolder
import dagger.hilt.internal.GeneratedComponentManager


class Applicate : MultiDexApplication(), GeneratedComponentManager<Any>, TestApplicationComponentManagerHolder {
  // This field is initialized in attachBaseContext to avoid pulling the generated component into
  // the main dex. We could possibly avoid this by class loading TestComponentDataSupplier lazily
  // rather than in the TestApplicationComponentManager constructor.
  private var componentManager: TestApplicationComponentManager? = null
  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    componentManager = TestApplicationComponentManager(this)
  }


  override fun onCreate() {
    super.onCreate()
    MMKV.initialize(this)

  }
  override fun componentManager(): Any {
    return componentManager!!
  }

  override fun generatedComponent(): Any {
    return componentManager!!.generatedComponent()
  }
}
