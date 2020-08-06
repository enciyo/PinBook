package com.enciyo.pinbook.ui.register

import com.enciyo.pinbook.reducer.ActionState
import com.enciyo.pinbook.reducer.SideEffect


sealed class RegisterActionState : ActionState() {
  object NavigateToMain : RegisterActionState()
  object NavigateToLogin : RegisterActionState()
  data class ShowResourceError(val message:Int): RegisterActionState()
  data class ShowError(val message:String): RegisterActionState()
}

data class RegisterSideEffect(
    val string:String = ""
) : SideEffect(){

}


