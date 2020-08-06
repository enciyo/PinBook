package com.enciyo.pinbook.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.enciyo.library.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.common.PinToast
import com.enciyo.pinbook.databinding.FragmentLoginBinding
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.utils.addTextChangedListener
import com.enciyo.pinbook.utils.safeErrorMessage
import com.enciyo.pinbook.utils.setOnClickListener
import com.enciyo.pinbook.utils.visibilityWithScaleAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {


  @Inject
  lateinit var mPinToast: PinToast

  val mBinding: FragmentLoginBinding by viewBinding()
  val mViewModel: LoginViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(mBinding) {
      editTextUsername.addTextChangedListener(::inputUsernameTextChanged, viewLifecycleOwner)
      editTextPassword.addTextChangedListener(::inputPasswordTextChanged, viewLifecycleOwner)
      buttonLogin.setOnClickListener(::buttonLoginClicked)
      buttonRegister.setOnClickListener(::buttonRegisterClicked)
    }


    with(mViewModel) {
      state
          .render(
              onState = ::viewUIState,
              onAction = ::actionStateObserver,
              onSideEffect = ::sideEffectObserver
          )
          .launchIn(viewLifecycleOwner.lifecycleScope)
    }
    navigateToMain()
  }

  private fun buttonLoginClicked() {
    mViewModel.onEvent(LoginUserIneractions.LoginClicked)
  }

  private fun buttonRegisterClicked() {
    mViewModel.onEvent(LoginUserIneractions.RegisterClicked)
  }

  private fun inputUsernameTextChanged(changedText: String) {
    mViewModel.onEvent(LoginUserIneractions.InputUsernameTextChanged(changedText))
  }

  private fun inputPasswordTextChanged(changedText: String) {
    mViewModel.onEvent(LoginUserIneractions.InputPasswordTextChanged(changedText))
  }


  private fun viewUIState(viewState: LoginViewState) {
    with(mBinding) {
      buttonLogin.visibilityWithScaleAnimation(viewState.isButtonLoginVisible, mBinding.root)
      progressBar.isVisible = viewState.isLoading
      inputUsername.isEnabled = viewState.isInputUsernameEnable
      inputPassword.isEnabled = viewState.isInputPasswordEnable
    }
    isUsernameError(viewState.isUsernameError)
    isPasswordError(viewState.isPasswordError)
  }

  private fun isPasswordError(isPasswordError: Boolean) {
    when (isPasswordError) {
      true -> mBinding.inputPassword.safeErrorMessage(R.string.ValidationErrorPassword)
      false -> mBinding.inputPassword.error = null
    }
  }

  private fun isUsernameError(isUsernameError: Boolean) {
    when (isUsernameError) {
      true -> mBinding.inputUsername.safeErrorMessage(R.string.ValidationErrorUsername)
      false -> mBinding.inputUsername.error = null
    }
  }


  private fun actionStateObserver(actionState: LoginActionState) {
    when (actionState) {
      is LoginActionState.NavigateToMain -> navigateToMain()
      is LoginActionState.NavigateToRegister -> navigateToRegister()
      is LoginActionState.ShowWelcomeMessage -> mPinToast.showSuccessMessage(actionState.message)
      is LoginActionState.ShowApiErrorMessage -> mPinToast.showErrorMessage(actionState.message)
      is LoginActionState.ShowUnknownErrorMessage -> mPinToast.showErrorMessage(actionState.message)
      is LoginActionState.ShowPopUp -> AlertDialog.Builder(requireContext()).setMessage("sada").show()
    }
  }

  private fun sideEffectObserver(sideEffect: LoginSideEffect) {
    sideEffect.user.observe(viewLifecycleOwner) {
      mPinToast.showErrorMessage(it.displayName.toString())
    }
  }


  private fun navigateToMain() {
    LoginFragmentDirections.actionLoginFragmentToMainFragment().also(findNavController()::navigate)
  }

  private fun navigateToRegister() {
    val extras = createFragmentNavigatorExtrasForRegisterTransition()
    val direction = LoginFragmentDirections.actionRegisterFragment()
    findNavController().navigate(direction, extras)
  }

  private fun createFragmentNavigatorExtrasForRegisterTransition() = with(mBinding) {
    return@with FragmentNavigatorExtras(
        logoImage to logoImage.transitionName,
        inputUsername to inputUsername.transitionName,
        inputPassword to inputPassword.transitionName,
        displayView to displayView.transitionName,
        emailView to emailView.transitionName,
        buttonLogin to buttonLogin.transitionName)
  }


}


