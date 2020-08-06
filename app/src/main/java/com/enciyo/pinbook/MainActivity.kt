package com.enciyo.pinbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enciyo.library.livedata.eventObserve
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.domain.PinBookRepository
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var mBookRepository: PinBookRepository

  @Inject
  lateinit var mPinToast:PinToast

  private val alerter by lazy {
    Alerter.create(this)
        .enableIconPulse(true)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mPinToast.toastMessage.eventObserve(this,::renderPinToastMessage)
  }

  private fun renderPinToastMessage(pinToast: PinToast.ToastType){
    when(pinToast){
      is PinToast.ToastType.SuccessMessage -> {
        Alerter.create(this@MainActivity)
            .enableIconPulse(true)
            .setBackgroundColorRes(R.color.green_500)
            .setText(pinToast.message)
            .show()
      }
      is PinToast.ToastType.ErrorMessage -> {
        Alerter.create(this@MainActivity)
            .enableIconPulse(true)
            .setBackgroundColorRes(R.color.red_500)
            .setText(pinToast.message)
            .show()
      }
    }
  }



}