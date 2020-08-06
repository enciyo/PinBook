package com.enciyo.pinbook.ui.splash



sealed class SplashActionState{
  object NavigateToLogin : SplashActionState()
  object NavigateToMain : SplashActionState()
  object ShowToastMessage: SplashActionState()
}