package com.enciyo.pinbook.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.databinding.FragmentRegisterBinding
import com.enciyo.pinbook.utils.addTextChangedListener
import com.enciyo.pinbook.utils.setOnClickListener
import com.enciyo.pinbook.utils.visibilityWithScaleAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

  @Inject
  lateinit var mPinToast: PinToast

  private val mBinding: FragmentRegisterBinding by viewBinding()
  private val mViewModel: RegisterViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)


    with(mBinding){
      inputUsername.editText?.addTextChangedListener(::onInputUsernameTextChanged)
      inputDisplayName.editText?.addTextChangedListener(::onInputDisplayTextChanged)
      inputEmail.editText?.addTextChangedListener(::onInputEmailTextChanged)
      inputPassword.editText?.addTextChangedListener(::onInputPasswordTextChanged)
      buttonRegister.setOnClickListener(::onButtonRegisterClicked)
      buttonLogin.setOnClickListener(::onButtonLoginClicked)
    }

    with(mViewModel){
      //viewState.eventObserve(viewLifecycleOwner,::viewStateObserver)
      //actionState.eventObserve(viewLifecycleOwner,::actionStateObserver)

      mReducer.state
          .onEach {
              when(it.view){
                null -> Unit
                else->viewStateObserver(it.view)
              }
              when(it.action){
                null->Unit
                else-> actionStateObserver(it.action.peekContent())
              }


          }
          .launchIn(viewLifecycleOwner.lifecycleScope)
    }

  }

  private fun onInputUsernameTextChanged(changedText:String){
    mViewModel.onEvent(RegisterViewEvents.InputUsernameTextChanged(changedText))
  }

  private fun onInputPasswordTextChanged(changedText: String){
    mViewModel.onEvent(RegisterViewEvents.InputPasswordTextChanged(changedText))
  }

  private fun onInputDisplayTextChanged(changedText: String){
    mViewModel.onEvent(RegisterViewEvents.InputDisplayNameTextChanged(changedText))
  }

  private fun onInputEmailTextChanged(changedText: String){
    mViewModel.onEvent(RegisterViewEvents.InputEmailTextChanged(changedText))
  }

  private fun onButtonRegisterClicked(){
    mViewModel.onEvent(RegisterViewEvents.RegisterClicked)
  }

  private fun onButtonLoginClicked(){
    mViewModel.onEvent(RegisterViewEvents.LoginClicked)
  }

  private fun viewStateObserver(viewState: RegisterViewState){
      renderLoading(viewState.isLoading)
  }



  private fun renderLoading(isLoading:Boolean){
    with(mBinding){
      buttonRegister.visibilityWithScaleAnimation(isLoading.not(),mBinding.root)
      progressBar.isVisible = isLoading
      inputUsername.isEnabled = isLoading.not()
      inputPassword.isEnabled = isLoading.not()
      inputDisplayName.isEnabled = isLoading.not()
      inputEmail.isEnabled = isLoading.not()
    }
  }

  private fun actionStateObserver(actionState: RegisterActionState){
    when(actionState){
      is RegisterActionState.NavigateToMain -> {}
      is RegisterActionState.NavigateToLogin -> findNavController().popBackStack()
      is RegisterActionState.ShowResourceError -> mPinToast.showErrorMessage(actionState.message)
      is RegisterActionState.ShowError -> mPinToast.showErrorMessage(actionState.message)
    }
  }



}


