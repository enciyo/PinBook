package com.enciyo.pinbook.ui.books.state

import com.enciyo.pinbook.reducer.UserInteractions


sealed class BookInteractions : UserInteractions() {
  object Init : BookInteractions()
  data class OnCategoriseClicked(val categoryId: String) : BookInteractions()
}






