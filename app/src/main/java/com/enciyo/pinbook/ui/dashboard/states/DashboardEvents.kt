package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.reducer.UserInteractions


sealed class DashboardEvents : UserInteractions() {
  object Init : DashboardEvents()
  data class ScrolledShowCaseBooks(val showCaseBook: Int) : DashboardEvents()
  object SwipedToRefresh : DashboardEvents()
}