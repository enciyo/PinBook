package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.reducer.ActionState


sealed class DashboardActionState  : ActionState() {
  data class ChangeShowCaseTitle(val showCaseBookName: String) : DashboardActionState()
  data class ShowErrorMessageFromResource(val message:Int) : DashboardActionState()
  data class ShowErrorMessage(val message:String) : DashboardActionState()
}