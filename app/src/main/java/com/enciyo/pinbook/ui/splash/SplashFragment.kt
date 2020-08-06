package com.enciyo.pinbook.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.enciyo.library.livedata.eventObserve
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

  @Inject
  lateinit var mPinToast: PinToast

  private val mBinding: FragmentSplashBinding by viewBinding()
  private val mViewModel: SplashViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(mViewModel) {
      viewState.eventObserve(viewLifecycleOwner, ::viewStateObserver)
      actionState.eventObserve(viewLifecycleOwner, ::actionStateObserver)
    }

    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
      mViewModel.checkServerIsAvailable()
    }

  }

  private fun viewStateObserver(viewState: SplashViewState) {

  }

  private fun actionStateObserver(actionState: SplashActionState) {
    when (actionState) {
      SplashActionState.NavigateToLogin -> SplashFragmentDirections.actionLoginFragment().also(findNavController()::navigate)
      SplashActionState.NavigateToMain -> SplashFragmentDirections.actionSplashFragmentToMainFragment().also(findNavController()::navigate)
    }
  }



}