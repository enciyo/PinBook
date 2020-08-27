package com.enciyo.pinbook.ui.fav

import com.enciyo.pinbook.reducer.UserInteractions


sealed class FavoriteUserInteractions : UserInteractions(){
  object Init: FavoriteUserInteractions()
}