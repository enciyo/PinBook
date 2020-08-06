package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.domain.model.ShowcaseBooks


sealed class ShowCaseViewState {
  data class ShowCaseSuccess(val showcaseBooks: MutableList<ShowcaseBooks>) : ShowCaseViewState() {
    fun getFirstItem() = showcaseBooks.firstOrNull()
        ?: throw NullPointerException("ShowcaseBooks not empty ")
  }

  object Loading : ShowCaseViewState()
  object Empty : ShowCaseViewState()
  object Failure: ShowCaseViewState()
}
