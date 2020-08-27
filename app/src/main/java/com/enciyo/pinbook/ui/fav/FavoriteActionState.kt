package com.enciyo.pinbook.ui.fav

import com.enciyo.pinbook.reducer.ActionState


sealed class FavoriteActionState : ActionState(){
  object Init: FavoriteActionState()
}