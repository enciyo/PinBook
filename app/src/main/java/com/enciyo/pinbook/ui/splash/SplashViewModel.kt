package com.enciyo.pinbook.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.PinBookCloudServerManagement
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.data.PinBookCacheDataSource
import com.enciyo.pinbook.utils.livedata.Event
import com.enciyo.pinbook.utils.livedata.postEvent
import com.enciyo.pinbook.utils.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import okhttp3.ResponseBody



class SplashViewModel @ViewModelInject constructor(
    private val mPinBookCacheDataSource: PinBookCacheDataSource,
    private val mPinBookCloudServerManagement: PinBookCloudServerManagement,
    private val mPinToast: PinToast
) : ViewModel() {

  private val _viewState: MutableLiveData<Event<SplashViewState>> = MutableLiveData()
  val viewState: LiveData<Event<SplashViewState>>
    get() = _viewState

  private val _actionState: MutableLiveData<Event<SplashActionState>> = MutableLiveData()
  val actionState: LiveData<Event<SplashActionState>>
    get() = _actionState


  fun checkServerIsAvailable() {
    checkUserIsThere()
    mPinBookCloudServerManagement.check()
        .onEach(::onHandleServerIsAvailableResult)
        .launchIn(viewModelScope)
  }

  private fun onHandleServerIsAvailableResult(result: AwesomeResult<ResponseBody>) {
    when (result) {
      is AwesomeResult.Success -> checkUserIsThere()
      is AwesomeResult.Loading -> Unit
      is AwesomeResult.UnknownError -> mPinToast.showErrorMessage(result.message)
      is AwesomeResult.ServerError -> mPinToast.showErrorMessage(result.errorData?.message?:return)
      is AwesomeResult.NoNetworkConnection -> mPinToast.showErrorMessage(R.string.NoConnection)
    }
  }


  private fun checkUserIsThere() {
    viewModelScope.launch {
      when (mPinBookCacheDataSource.currentUser()) {
        null -> {
          _actionState.postEvent(SplashActionState.NavigateToLogin)
        }
        else -> {
          _actionState.postEvent(SplashActionState.NavigateToMain)
        }
      }
    }
  }


}